package org.sbolstandard.core3.entity;

import java.net.URI;
import java.util.List;

import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.Resource;
import org.sbolstandard.core3.util.RDFUtil;
import org.sbolstandard.core3.util.SBOLGraphException;
import org.sbolstandard.core3.util.SBOLUtil;
import org.sbolstandard.core3.validation.IdentifiedValidator;
import org.sbolstandard.core3.validation.PropertyValidator;
import org.sbolstandard.core3.validation.ValidationMessage;
import org.sbolstandard.core3.vocabulary.DataModel;

import jakarta.validation.constraints.NotNull;

public class ComponentReference extends Feature{
	/*private URI feature=null;
	private URI inChildOf=null;*/
	
	protected  ComponentReference(Model model,URI uri) throws SBOLGraphException
	{
		super(model, uri);
	}
	
	protected  ComponentReference(Resource resource) throws SBOLGraphException
	{
		super(resource);
	}

	@NotNull(message = "{COMPONENTREFERENCE_REFERSTO_NOT_NULL}") 
	public Feature getRefersTo() throws SBOLGraphException {
		//return IdentifiedValidator.getValidator().getPropertyAsURI(this.resource, DataModel.ComponentReference.refersTo);
		return contsructIdentified(DataModel.ComponentReference.refersTo, Feature.getSubClassTypes());	
	}
	
	public void setRefersTo(@NotNull(message = "{COMPONENTREFERENCE_REFERSTO_NOT_NULL}") Feature feature) throws SBOLGraphException {
		PropertyValidator.getValidator().validate(this, "setRefersTo", new Object[] {feature}, Feature.class);
		RDFUtil.setProperty(this.resource, DataModel.ComponentReference.refersTo, SBOLUtil.toURI(feature));
	}
	
	@NotNull(message = "{COMPONENTREFERENCE_INCHILDOF_NOT_NULL}")
	public SubComponent getInChildOf() throws SBOLGraphException{
		//return IdentifiedValidator.getValidator().getPropertyAsURI(this.resource, DataModel.ComponentReference.inChildOf);
		return contsructIdentified(DataModel.ComponentReference.inChildOf, SubComponent.class, DataModel.SubComponent.uri);
	}

	public void setInChildOf(@NotNull(message = "{COMPONENTREFERENCE_INCHILDOF_NOT_NULL}") SubComponent inChildOf) throws SBOLGraphException{
		PropertyValidator.getValidator().validate(this, "setInChildOf", new Object[] {inChildOf}, SubComponent.class);
		RDFUtil.setProperty(this.resource, DataModel.ComponentReference.inChildOf, SBOLUtil.toURI(inChildOf));	
	}
	
	@Override
	public URI getResourceType() {
		return DataModel.ComponentReference.uri;
	}
	
	@Override
	public List<ValidationMessage> getValidationMessages() throws SBOLGraphException
	{
		List<ValidationMessage> validationMessages=super.getValidationMessages();
		validationMessages= IdentifiedValidator.assertEquals(this, DataModel.ComponentReference.refersTo, this.resource, getRefersTo(), validationMessages);
		validationMessages= IdentifiedValidator.assertEquals(this, DataModel.ComponentReference.inChildOf, this.resource, this.getInChildOf(), validationMessages);
		
		Feature referredFeature=this.getRefersTo();
		if (referredFeature!=null && referredFeature instanceof ComponentReference)
		{
			ComponentReference referredCompRef=(ComponentReference) referredFeature;
			SubComponent childSubComponent=referredCompRef.getInChildOf();
			
			SubComponent subComponent=this.getInChildOf();
			if (subComponent!=null)
			{
				Component parentComponent=subComponent.getInstanceOf();
				
				ValidationMessage message = new ValidationMessage("{COMPONENTREFERENCE_INCHILDOF_SUBCOMPONENT_VALID}", DataModel.ComponentReference.refersTo,referredCompRef, childSubComponent.getUri());
				message.childPath(DataModel.ComponentReference.inChildOf);
				validationMessages= IdentifiedValidator.assertExists(this, validationMessages, childSubComponent, parentComponent.getSubComponents(), message);			
			
				message = new ValidationMessage("{COMPONENTREFERENCE_REFERSTO_CHILDCOMPONENTREFERENCE_VIA_SUBCOMPONENT}", DataModel.ComponentReference.refersTo, referredCompRef);
				validationMessages= IdentifiedValidator.assertExists(this, validationMessages, referredCompRef, parentComponent.getComponentReferences(), message);				
			}
		}
		else if (referredFeature!=null && !(referredFeature instanceof ComponentReference))
		{
			SubComponent subComponent=this.getInChildOf();
			if (subComponent!=null)
			{
				Component parentComponent=subComponent.getInstanceOf();	
				ValidationMessage message = new ValidationMessage("{COMPONENTREFERNCE_REFERSTO_FEATURE_VIA_SUBCOMPONENT}", DataModel.ComponentReference.refersTo, referredFeature);
				validationMessages= IdentifiedValidator.assertExists(this, validationMessages, referredFeature, parentComponent.getFeatures(), message);				
			}
		}
		
		return validationMessages;
	}
}