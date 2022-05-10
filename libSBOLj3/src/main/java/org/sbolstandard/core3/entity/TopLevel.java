package org.sbolstandard.core3.entity;

import java.net.URI;
import java.util.List;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.sparql.function.library.namespace;
import org.sbolstandard.core3.util.RDFUtil;
import org.sbolstandard.core3.util.SBOLGraphException;
import org.sbolstandard.core3.util.SBOLUtil;
import org.sbolstandard.core3.validation.IdentifiedValidator;
import org.sbolstandard.core3.validation.PropertyValidator;
import org.sbolstandard.core3.validation.ValidationMessage;
import org.sbolstandard.core3.vocabulary.DataModel;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

public abstract class TopLevel extends Identified {

	/*private List<URI> attachments=null;
	private URI namespace=null;*/
	
	
	protected TopLevel(Model model, URI uri) throws SBOLGraphException
	{
		super(model, uri);
	}
	protected TopLevel() 
	{
		
	}
	
	protected TopLevel (Resource resource) throws SBOLGraphException
	{
		super(resource);
	}
	
	@Override
	public List<ValidationMessage> getValidationMessages() throws SBOLGraphException
	{
		List<ValidationMessage> validationMessages=super.getValidationMessages();
		
    	String nameSpace=null;
    	
    	if (this.getNamespace()!=null){
    		nameSpace=this.getNamespace().toString().toLowerCase();
    	}
    	URI uri=this.getUri();
    	String uriString=uri.toString().toLowerCase();
    	if (!uriString.startsWith("urn") && nameSpace!=null){
    		if (!uriString.startsWith(nameSpace)){
    			validationMessages= addToValidations(validationMessages,new ValidationMessage("{TOPLEVEL_URI_STARTS_WITH_NAMESPACE}", DataModel.TopLevel.namespace, this.getNamespace()));      	
    		}
    		else
    		{
    			boolean invalid=false;
    			if (nameSpace.endsWith("/"))
    			{
    				invalid=true;
    			}
    			else{
    				String localPath=uriString.replace(nameSpace, "");
    				//The path must have at least one / character. The local fragment is optional.
    				String[] fragments= localPath.split("/");
    				if (fragments.length>3 || fragments.length==1){
    					invalid=true;
    				}
    				else if (fragments.length==4 && (fragments[1].length()==0 || fragments[2].length()==0 | fragments[3].length()==0)){
    					invalid=true;
    				}
    			}
    			
    			if (invalid)
    			{
        		validationMessages= addToValidations(validationMessages,new ValidationMessage("{TOPLEVEL_URI_PATTERN}", DataModel.TopLevel.namespace, this.getNamespace()));      			
    			}
    		}
    		
    	}
    	return validationMessages;
	}
	
	public List<URI> getAttachments() {
		return RDFUtil.getPropertiesAsURIs(this.resource, DataModel.TopLevel.attachment);
	}
	
	public void setAttachments(List<URI> attachments) {
		RDFUtil.setProperty(resource, DataModel.TopLevel.attachment, attachments);
	}
	
	@NotNull(message = "{TOPLEVEL_NAMESPACE_NOT_NULL}")
	public URI getNamespace() throws SBOLGraphException{
		return IdentifiedValidator.getValidator().getPropertyAsURI(this.resource, DataModel.TopLevel.namespace);	
	}

	public void setNamespace(@NotNull(message = "{TOPLEVEL_NAMESPACE_NOT_NULL}") URI namespace) throws SBOLGraphException {
		PropertyValidator.getValidator().validate(this, "setNamespace", new Object[] {namespace}, URI.class);
		/*if (namespace==null)
		{
			throw new SBOLGraphException("Namespace cannot be null. URI:" + this.resource.getURI());
		}*/
		/*URI newURI=null;
		if (namespace!=null){
			String uriString= namespace.toString();
			
			if (SBOLUtil.isURL(uriString))
			{
				if (uriString.endsWith("/") || uriString.endsWith("#"))
				{
					uriString=uriString.substring(0, uriString.length()-1);
				}
			}
			newURI=URI.create(uriString);
		}
		RDFUtil.setProperty(resource, DataModel.TopLevel.namespace, newURI);*/
		RDFUtil.setProperty(resource, DataModel.TopLevel.namespace, namespace);
	}
	
	
	 
	@Override
	public URI getResourceType() {
		return DataModel.TopLevel.uri;
	}
}
