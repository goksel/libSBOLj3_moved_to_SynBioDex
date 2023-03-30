package org.sbolstandard.core3.validation;

import java.net.URI;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.vocabulary.RDF;
import org.sbolstandard.core3.entity.Identified;
import org.sbolstandard.core3.util.Configuration;
import org.sbolstandard.core3.util.RDFUtil;
import org.sbolstandard.core3.util.SBOLGraphException;
import org.sbolstandard.core3.util.SBOLUtil;
import org.sbolstandard.core3.util.URINameSpace;
import org.sbolstandard.core3.vocabulary.ComponentType;
import org.sbolstandard.core3.vocabulary.DataModel;
import org.sbolstandard.core3.vocabulary.MeasureDataModel;
import org.sbolstandard.core3.vocabulary.ComponentType.StrandType;
import org.sbolstandard.core3.vocabulary.ComponentType.TopologyType;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;

public class IdentifiedValidator {
	//private static IdentifiedValidator idendityValidator = null;
	protected Validator validator;

	private IdentifiedValidator() throws SBOLGraphException {
		try {
			ValidatorFactory factory = Validation.byDefaultProvider().configure()
					// .addValueExtractor(new ...ValueExtractor())
					.buildValidatorFactory();
			this.validator = factory.getValidator();
		} catch (Exception exception) {
			throw new SBOLGraphException("Could not initialize the validator", exception);
		}
	}

	/*public static IdentifiedValidator getValidator() throws SBOLGraphException {
		if (idendityValidator == null) {
			try {
				idendityValidator = new IdentifiedValidator();
				ValidatorFactory factory = Validation.byDefaultProvider().configure()
						// .addValueExtractor(new ...ValueExtractor())
						.buildValidatorFactory();
				idendityValidator.validator = factory.getValidator();
			} catch (Exception exception) {
				throw new SBOLGraphException("Could not initialize the validator", exception);
			}
		}
		return idendityValidator;
	}*/
	
	//https://itzone.com.vn/en/article/bill-pugh-singleton-in-java-incredibly-simple/
	//https://www.geeksforgeeks.org/java-singleton-design-pattern-practices-examples/?ref=lbp
	public static IdentifiedValidator getValidator() throws SBOLGraphException {
		return SingletonHelper.INSTANCE;
	}

	private static class SingletonHelper {
        private static final IdentifiedValidator INSTANCE ;
        static {
            try {
                INSTANCE = new IdentifiedValidator();
            } catch (SBOLGraphException e) {
                throw new ExceptionInInitializerError(e);
            }
        }
    }
	
	public List<String> validate(Identified identified) {
		Set<ConstraintViolation<Identified>> violations = validator.validate(identified);
		List<String> messages = PropertyValidator.getViolotionMessages(violations);
		/*
		 * List<String> messages=new ArrayList<String>(); for
		 * (ConstraintViolation<Identified> violation : violations) { List<String>
		 * fragments=new ArrayList<String>(); fragments.add(violation.getMessage());
		 * fragments.add(String.format("Property: %s",violation.getPropertyPath().
		 * toString())); if (violation.getLeafBean()!=null && violation.getLeafBean()
		 * instanceof Identified ){ Identified identifiedLeaf= (Identified)
		 * violation.getLeafBean();
		 * fragments.add(String.format("Entity URI: %s",identifiedLeaf.getUri().toString
		 * ()));
		 * fragments.add(String.format("Entity type: %s",identifiedLeaf.getClass())); }
		 * if (violation.getInvalidValue()!=null && !(violation.getInvalidValue()
		 * instanceof Identified)){ fragments.add("Value:" +
		 * violation.getInvalidValue().toString()); } String
		 * message=StringUtils.join(fragments, "," + System.lineSeparator() + "\t");
		 * messages.add(message); }
		 */
		return messages;
	}

	public static <T extends Identified> List<ValidationMessage> assertExists(Identified identified, List<ValidationMessage> messages, URI uri, List<T> identifieds, String errorMessage, URI property) {
		if (uri != null) {
			if (!SBOLUtil.exists(uri, identifieds)) {
				messages = identified.addToValidations(messages, new ValidationMessage(errorMessage, property, uri));
			}
		}
		return messages;
	}

	public static <T extends Identified> List<ValidationMessage> assertExists(Identified identified, List<ValidationMessage> messages, URI uri, List<T> identifieds, ValidationMessage message) {
		if (uri != null) {
			if (!SBOLUtil.exists(uri, identifieds)) {
				messages = identified.addToValidations(messages, message);
			}
		}
		return messages;
	}
	
	public static <T extends Identified> List<ValidationMessage> assertExists(Identified identified, List<ValidationMessage> messages, Identified target, List<T> identifieds, ValidationMessage message) {
		if (target != null) {
			messages=assertExists(identified, messages, target.getUri(), identifieds, message);
		}
		return messages;
	}

	
	public static <T extends Identified> List<ValidationMessage> assertExistsIdentifieds(Identified identified, List<ValidationMessage> messages, List<T> targets, List<T> identifieds, ValidationMessage message) {
		if (targets != null) {
			messages = assertExists(identified, messages, SBOLUtil.getURIs(targets), identifieds,message);
		}
		return messages;
	}
	
	public static <T extends Identified> List<ValidationMessage> assertExists(Identified identified, List<ValidationMessage> messages, List<URI> uriList, List<T> identifieds, String errorMessage, URI property) {
		if (uriList != null) {
			for (URI uri : uriList) {
				messages = assertExists(identified, messages, uri, identifieds, errorMessage, property);
			}
		}
		return messages;
	}

	public static <T extends Identified> List<ValidationMessage> assertExists(Identified identified, List<ValidationMessage> messages, List<URI> uriList, List<T> identifieds, ValidationMessage message) {
		if (uriList != null) {
			for (URI uri : uriList) {
				message.setInvalidValue(uri);
				messages = assertExists(identified, messages, uri, identifieds, message);
			}
		}
		return messages;
	}

	public static <T extends Identified> List<ValidationMessage> assertExists(Identified parent, URI propertyURI, Resource resource, List<T> identifieds, List<ValidationMessage> messages) {
		List<URI> uris = RDFUtil.getPropertiesAsURIs(resource, propertyURI);
		if (uris != null && uris.size()>0) {
			if (identifieds==null || identifieds.size()==0)
			{
				ValidationMessage message = new ValidationMessage("{SBOL_VALID_ENTITY_TYPES}", propertyURI, uris);
				messages = IdentifiedValidator.addToValidations(messages, message);
			}
			else if (identifieds!=null)
			{
				List<URI> identifiedURIs=SBOLUtil.getURIs(identifieds);
				for (URI uri: uris)
				{
					if (!identifiedURIs.contains(uri))
					{
						ValidationMessage message = new ValidationMessage("{SBOL_VALID_ENTITY_TYPES}", propertyURI, uri);
						messages = IdentifiedValidator.addToValidations(messages, message);
					}
				}
			}
		}
		return messages;
	}
	
	public static <T extends Identified> List<ValidationMessage> assertEquals(Identified parent, URI propertyURI, Resource resource, Identified child, List<ValidationMessage> messages) {
		List<URI> uris = RDFUtil.getPropertiesAsURIs(resource, propertyURI);
		if (uris != null && uris.size()>0) {
			if (child==null || !child.getUri().equals(uris.get(0)) || uris.size()>1)
			{
				ValidationMessage message = new ValidationMessage("{SBOL_VALID_ENTITY_TYPES}", propertyURI, uris);
				messages = IdentifiedValidator.addToValidations(messages, message);
			}
		}
		return messages;
	}
	
	
	public static <T extends Identified> List<ValidationMessage> assertURIStartsWith(Identified identified, List<ValidationMessage> messages, List<T> identifieds) throws SBOLGraphException {
		if (identifieds != null) {
			String targetURI = identified.getUri().toString() + "/";
			for (Identified identifiedTarget : identifieds) {
				if (!identifiedTarget.getUri().toString().toLowerCase().startsWith(targetURI.toLowerCase())) {
					ValidationMessage message = new ValidationMessage("{IDENTIFIED_URI_MUST_BE_USED_AS_A_PREFIX_FOR_CHILDREN}", DataModel.Identified.uri, identifiedTarget, identifiedTarget);
					// message.childPath(DataModel.Identified.uri, identifiedTarget);
					messages = IdentifiedValidator.addToValidations(messages, message);
				}
			}
		}
		return messages;
	}

	public static <T extends Identified> List<ValidationMessage> assertNotEqual(Identified identified, List<ValidationMessage> messages, Identified target1, Identified target2, String errorMessage, URI property) {
		if (target1 != null && target2!=null) {
			messages = assertNotEqual(identified, messages, target1.getUri(), target2.getUri(), errorMessage, property);
		}
		return messages;
	}
	
	public static <T extends Identified> List<ValidationMessage> assertNotEqual(Identified identified, List<ValidationMessage> messages, URI uri1, URI uri2, String errorMessage, URI property) {
		if (uri1 != null && uri2 != null && uri1.equals(uri2)) {
			messages = identified.addToValidations(messages, new ValidationMessage(errorMessage, property));
		}

		return messages;
	}
	
	public static <T extends Identified> List<ValidationMessage> assertNotEqual(Identified identified, List<ValidationMessage> messages, URI uri1, URI uri2, ValidationMessage message) {
		if (uri1 != null && uri2 != null && uri1.equals(uri2)) {
			messages = identified.addToValidations(messages, message);
		}

		return messages;
	}

	public static <T extends Identified> List<ValidationMessage> assertNotEqual(Identified identified, List<ValidationMessage> messages, Identified identified1, Identified identified2, ValidationMessage message) {
		if (identified1 != null && identified2 != null) {
			return assertNotEqual(identified, messages, identified1.getUri(), identified2.getUri(), message);
		}
		return messages;
	}

	
	 public static List<ValidationMessage> assertAtMostOneTopologyType(List<URI> types, List<ValidationMessage> validationMessages, String message)
	    {
	    	int counter=0;
			if (types!=null && (types.contains(ComponentType.DNA.getUri()) || types.contains(ComponentType.RNA.getUri()))){
				for(URI typeURI: types) {
					TopologyType topologyType = TopologyType.get(typeURI);
					if (topologyType!=null)
					{
						counter++;
					}
				}
				if(counter>1){
					validationMessages= IdentifiedValidator.addToValidations(validationMessages,new ValidationMessage(message, DataModel.type, types));      		
				}
			}
			return validationMessages;
	    }
	 
		public static List<ValidationMessage> assertOnlyDNAOrRNAComponentsIncludeStrandOrTopology(List<URI> types, List<ValidationMessage> validationMessages, String message) {
			boolean checkDNAOrRNA = false;
			if (types!=null)
			{
				for (URI typeURI : types) {
					TopologyType topologyType = TopologyType.get(typeURI);
					if (topologyType != null) {
						checkDNAOrRNA = true;
						break;
					} else {
						StrandType strandType = StrandType.get(typeURI);
						if (strandType != null) {
							checkDNAOrRNA = true;
							break;
						}
					}
				}
	
				if (checkDNAOrRNA) {
					if (!types.contains(ComponentType.DNA.getUri()) && !types.contains(ComponentType.RNA.getUri())) {
						validationMessages = IdentifiedValidator.addToValidations(validationMessages, new ValidationMessage(message, DataModel.type, types));
					}
				}
			}
			return validationMessages;
		}

		
		/*public static Pair<List<ValidationMessage>, Map<URI, Boolean>>  assertOnlyDNAOrRNAIdentifiedsIncludeSOFeatureRole(List<URI> types, List<URI> roles, List<ValidationMessage> validationMessages, ValidationMessage message, Map<URI, Boolean> validity) {
			if (types!=null && roles!=null && !types.contains(ComponentType.DNA.getUri()) && !types.contains(ComponentType.RNA.getUri())) {
				for (URI role: roles)
				{
					Boolean valid = validity.get(role);
					if (valid == null) {
						valid = RDFUtil.hasParentRecursively(Configuration.getInstance().getSOOntology(), role.toString(), Role.SequenceFeature.toString());
						validity.put(role, valid);
					}
					if (valid) {
						message.setInvalidValue(role);
						validationMessages = IdentifiedValidator.addToValidations(validationMessages, message);
					}
				}
			}
			return Pair.of(validationMessages, validity);
		}*/
		
		public static List<ValidationMessage> assertOnlyDNAOrRNAIdentifiedsIncludeSOFeatureRole(List<URI> types, Set<String> sequenceFeatures, List<URI> roles, List<ValidationMessage> validationMessages, String message, URI entityType, Identified entity) throws SBOLGraphException {
			if (types!=null && roles!=null && !types.contains(ComponentType.DNA.getUri()) && !types.contains(ComponentType.RNA.getUri())) {
				validationMessages= assertDoesNotExists(sequenceFeatures, roles, validationMessages, message, entityType, entity, DataModel.role);
			}
			return validationMessages;
		}
		
		public static List<ValidationMessage> assertDoesNotExists(Set<String> items, List<URI> searchItems, List<ValidationMessage> validationMessages, String message, URI entityType, Identified entity, URI property) throws SBOLGraphException {
			if (searchItems!=null){
			for (URI uri: searchItems)
				{
				if (items.contains(uri.toString()))
				{
					ValidationMessage validationMessage = new ValidationMessage(message, property, uri);
					/*ValidationMessage validationMessage = new ValidationMessage(message, DataModel.Component.uri, entity, uri);
					validationMessage.childPath(property);*/
					validationMessages = IdentifiedValidator.addToValidations(validationMessages, validationMessage);
				}
			}
			}
			return validationMessages;
		}
		
		public static List<ValidationMessage> assertOneExists(Set<String> items, List<URI> searchURIs, List<ValidationMessage> validationMessages, String message, Identified entity, URI property) throws SBOLGraphException
		{
			int count=0;
			if (searchURIs!=null)
			{
				for (URI uri:searchURIs)
				{
					if (items.contains(uri.toString()))
					{
						count++;
					}
				}
			}
			if (count!=1)
			{
				ValidationMessage validationMessage= new ValidationMessage(message, property, searchURIs);
				validationMessages = IdentifiedValidator.addToValidations(validationMessages, validationMessage);
			}
			return validationMessages;
		}
		
		public static Set<URI> getMatchingSearchURIs(Collection<URI> items, Collection<URI> toSearchURIs) throws SBOLGraphException
		{
			Set<URI> result=null;
			if (items!=null && toSearchURIs!=null)
			{
				for (URI uri:toSearchURIs){
					if (items.contains(uri)){
						if (result==null)
						{
							result=new HashSet<URI>();
						}
						result.add(uri);
					}
				}
			}
			return result;
		}
		
		public static List<ValidationMessage> assertAtMostOneExists(Set<String> items, List<URI> searchURIs, List<ValidationMessage> validationMessages, String message, Identified entity, URI property) throws SBOLGraphException
		{
			int count=0;
			if (searchURIs!=null)
			{
				for (URI uri:searchURIs)
				{
					if (items.contains(uri.toString()))
					{
						count++;
					}
				}
			}
			if (count>1)
			{
				ValidationMessage validationMessage= new ValidationMessage(message, property, searchURIs);
				validationMessages = IdentifiedValidator.addToValidations(validationMessages, validationMessage);
			}
			return validationMessages;
		}
		
		public static List<ValidationMessage> assertIfDNAOrRNAThenIdentifiedShouldIncludeOneSOFeatureRole(List<URI> types, List<URI> roles, List<ValidationMessage> validationMessages, String message, Identified entity) throws SBOLGraphException {
			if (types!=null && (types.contains(ComponentType.DNA.getUri()) || types.contains(ComponentType.RNA.getUri()))) 
			{
				validationMessages=assertOneExists(Configuration.getInstance().getSoSequenceFeatures(), roles, validationMessages, message, entity, DataModel.role);
			}
			return validationMessages;
		}
			
		/*public static Pair<List<ValidationMessage>, Map<URI, Boolean>>  assertIfDNAOrRNAThenIdentifiedShouldIncludeAtMostOneSOFeatureRole(List<URI> types, List<URI> roles, List<ValidationMessage> validationMessages, String message, Map<URI, Boolean> validity, URI entityType, Identified entity) throws SBOLGraphException {
			if (types!=null && (types.contains(ComponentType.DNA.getUri()) || types.contains(ComponentType.RNA.getUri()))) {
				int validRoles=0;
				if (roles!=null && roles.size()>0)
				{
					for (URI role: roles)
					{
						Boolean valid = validity.get(role);
						if (valid == null) {
							valid = RDFUtil.hasParentRecursively(Configuration.getInstance().getSOOntology(), role.toString(), Role.SequenceFeature.toString());
							validity.put(role, valid);
						}
						if (valid) {
							
							validRoles++;
							if (validRoles>1)
							{
								ValidationMessage validationMessage= new ValidationMessage(message, entityType, entity, role);
								validationMessage.childPath(DataModel.role);
								validationMessages = IdentifiedValidator.addToValidations(validationMessages, validationMessage);
							}
						}
					}
				}
				if (validRoles==0)
				{
					ValidationMessage validationMessage=null;
					if (roles!=null && roles.size()>0) 
					{
						validationMessage = new ValidationMessage(message, entityType, entity, roles);
					}
					else
					{
						validationMessage = new ValidationMessage(message, entityType, entity, null);
					}
					validationMessage.childPath(DataModel.role);
					validationMessages = IdentifiedValidator.addToValidations(validationMessages, validationMessage);
				}
			}
			return Pair.of(validationMessages, validity);
		}
		*/
			
	/*
	 * public List<String> validate2(Identified identified) {
	 * Set<ConstraintViolation<Identified>> violations =
	 * validator.validate(identified); List<String> messages=new
	 * ArrayList<String>(); for (ConstraintViolation<Identified> violation :
	 * violations) { String
	 * propertyMessage=String.format("Property: %s",violation.getPropertyPath().
	 * toString()); String entityMessage
	 * =String.format("Entity Type: %s",violation.getRootBeanClass().getName());
	 * String uriMessage=""; if (violation.getRootBeanClass()!=null) { URI
	 * uri=((Identified) violation.getRootBean()).getUri(); uriMessage
	 * =String.format("Entity: %s",uri.toString()); } String
	 * message=String.format("%s, %s, %s, %s", violation.getMessage(),
	 * entityMessage, propertyMessage, uriMessage); messages.add(message); } return
	 * messages; }
	 */

	public List<ValidationMessage> assertOneSBOLEntityType(Identified identified, Resource resource, List<ValidationMessage> messages) throws SBOLGraphException {
		List<URI> uris = RDFUtil.getRDFTypes(resource, URINameSpace.SBOL.getUri());
		if (uris != null && uris.size() > 1) {
			ValidationMessage message = new ValidationMessage("{IDENTIFIED_SUITABLE_SBOL_ENTITY_TYPES}", URI.create(RDF.type.getURI()), identified, uris);
			messages = IdentifiedValidator.addToValidations(messages, message);
		}
		return messages;
	}
	
	public static List<ValidationMessage> assertTwoPropertyValueIdenticalEqual(List<ValidationMessage> validationMessages, String message, String first, String second, URI firstPropertyURI, URI secondPropertyURI) throws SBOLGraphException
	{
		if (first!=null && !first.isEmpty() && second!=null && !second.isEmpty()) {
			if (!first.equals(second)){
				String messageString=String.format("%s%s%s: %s",message,ValidationMessage.INFORMATION_SEPARATOR, secondPropertyURI, second);				
				ValidationMessage valMessage=new ValidationMessage(messageString, firstPropertyURI, first);  				
				validationMessages= IdentifiedValidator.addToValidations(validationMessages,valMessage);											
			}
		}
		return validationMessages;
	}

	public URI getPropertyAsURI(Resource resource, URI property) throws SBOLGraphException {
		URI result = null;
		if (Configuration.getInstance().enforceOneToOneRelationships()) {
			List<URI> uris = RDFUtil.getPropertiesAsURIs(resource, property);
			if (uris != null && uris.size() > 0) {
				if (uris.size() > 1) {
					String message = String.format("Multiple values are included for property %s. Entity URI:%s", property.toString(), resource.getURI());
					throw new SBOLGraphException(message);
				} else {
					result = uris.get(0);
				}
			}
		} else {
			result = RDFUtil.getPropertyAsURI(resource, property);
		}
		return result;
	}

	public String getPropertyAsString(Resource resource, URI property) throws SBOLGraphException {
		String result = null;
		if (Configuration.getInstance().enforceOneToOneRelationships()) {
			List<String> uris = RDFUtil.getLiteralPropertiesAsStrings(resource, property);
			if (uris != null && uris.size() > 0) {
				if (uris.size() > 1) {
					String message = String.format("Multiple values are included for property %s. URI:%s", property.toString(), resource.getURI());
					throw new SBOLGraphException(message);
				} else {
					result = uris.get(0);
				}
			}
		} else {
			result = RDFUtil.getPropertyAsString(resource, property);
		}
		return result;
	}

	/*
	 * public OptionalInt getPropertyAsOptionalInt(Resource resource, URI property)
	 * throws SBOLGraphException { OptionalInt result=OptionalInt.empty(); String
	 * value=IdentityValidator.getValidator().getPropertyAsString(resource,
	 * property); if (value!=null){ result=OptionalInt.of(Integer.valueOf(value)); }
	 * return result; }
	 */

	public Optional<Integer> getPropertyAsOptionalInteger(Resource resource, URI property) throws SBOLGraphException {
		Optional<Integer> result = Optional.empty();
		String value = IdentifiedValidator.getValidator().getPropertyAsString(resource, property);
		if (value != null) {
			result = Optional.of(Integer.valueOf(value));
		}
		return result;
	}

	/*
	 * public void setPropertyAsOptionalInt(Resource resource, URI property,
	 * OptionalInt value) { String stringValue=null; if (value!=null &&
	 * value.isPresent()) { stringValue= String.valueOf(value.getAsInt()); }
	 * RDFUtil.setProperty(resource, property, stringValue); }
	 */

	public Optional<Float> getPropertyAsOptionalFloat(Resource resource, URI property) throws SBOLGraphException {
		Optional<Float> result = Optional.empty();
		String value = IdentifiedValidator.getValidator().getPropertyAsString(resource, property);
		if (value != null) {
			try {
				result = Optional.of(Float.parseFloat(value));
			} catch (Exception e) {
				throw new SBOLGraphException("Cannot read the value. Property:" + MeasureDataModel.Measure.value + " Uri:+ " + resource.getURI(), e);
			}
		}
		return result;
	}

	/*
	 * public void setPropertyAsOptionalFloat(Resource resource, URI property,
	 * Optional<Float> value) { String stringValue=null; if (value!=null &&
	 * value.isPresent()) { stringValue= String.valueOf(value.get()); }
	 * RDFUtil.setProperty(resource, property, stringValue); }
	 */

	public void setPropertyAsOptional(Resource resource, URI property, Optional<?> value) {
		String stringValue = null;
		if (value != null && value.isPresent()) {
			stringValue = String.valueOf(value.get());
		}
		RDFUtil.setProperty(resource, property, stringValue);
	}

	public static List<ValidationMessage> addToValidations(List<ValidationMessage> messages, ValidationMessage message) {
		if (messages == null) {
			messages = new ArrayList<ValidationMessage>();
		}
		messages.add(message);
		return messages;

	}

	

	/*
	 * public Optional<?> getPropertyAsOptional(Resource resource, URI property)
	 * throws SBOLGraphException { Optional<?> result=Optional.empty(); String
	 * value=IdentityValidator.getValidator().getPropertyAsString(resource,
	 * property); if (value!=null){ result=Optional.of(value); } return result; }
	 * 
	 * public void setPropertyAsOptional(Resource resource, URI property,
	 * Optional<?> value) { String stringValue=null; if (value.isPresent()) {
	 * stringValue= String.valueOf(value.get()); } RDFUtil.setProperty(resource,
	 * property, stringValue); }
	 */

	/*
	 * public String getRequiredPropertyAsString(Resource resource, URI property)
	 * throws SBOLGraphException { String value=getPropertyAsString(resource,
	 * property); if (value==null) { throw new
	 * SBOLGraphException("Cannot read the value. Property:" + property+ " Uri:+ " +
	 * resource.getURI());
	 * 
	 * } return value;
	 * 
	 * }
	 */

}
