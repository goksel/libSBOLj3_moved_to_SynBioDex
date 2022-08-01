package org.sbolstandard.core3.entity;

import java.net.URI;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.Resource;
import org.sbolstandard.core3.util.RDFUtil;
import org.sbolstandard.core3.util.SBOLGraphException;
import org.sbolstandard.core3.validation.IdentifiedValidator;
import org.sbolstandard.core3.validation.ValidationMessage;
import org.sbolstandard.core3.vocabulary.DataModel;
import org.sbolstandard.core3.vocabulary.Encoding;

public class Sequence extends TopLevel {
	/*private String elements;
	private Encoding encoding;*/

	protected  Sequence(Model model,URI uri) throws SBOLGraphException
	{
		super(model, uri);
	}
	
	protected  Sequence(Resource resource) throws SBOLGraphException
	{
		super(resource);
	}

	@Override
	public List<ValidationMessage> getValidationMessages() throws SBOLGraphException
	{
		List<ValidationMessage> validationMessages=super.getValidationMessages();
		if (this.getElements()!=null && this.getEncoding()==null)
		{
			validationMessages= addToValidations(validationMessages,new ValidationMessage("{SEQUENCE_MUST_HAVE_ENCODING}", DataModel.Sequence.encoding));      	   
		}
		validationMessages = checkEncodingType(validationMessages);
    	return validationMessages;
	}
	
	private List<ValidationMessage> checkEncodingType(List<ValidationMessage> validationMessages) throws SBOLGraphException {

		String elements = this.getElements();
		Encoding enc = this.getEncoding();
		if (elements!=null && !elements.isEmpty() && enc != null) {
			if (enc.equals(Encoding.AminoAcid)) {
				Pattern patternAA = Pattern.compile("^[ARNDCQEGHILKMFPSTWYVX]+$", Pattern.CASE_INSENSITIVE); // compiled from list of characters at https://iupac.qmul.ac.uk/AminoAcid/AA1n2.html
				Matcher matcherAA = patternAA.matcher(elements);
				if (!matcherAA.find()) {
					validationMessages = addToValidations(validationMessages, new ValidationMessage(
							"{SEQUENCE_ELEMENTS_CONSISTENT_WITH_ENCODING}", DataModel.Sequence.elements));
				}
			} else if (enc.equals(Encoding.INCHI)) {
				Pattern patternINCHI = Pattern.compile("^((InChI=)?[^J][0-9a-z+\\-\\(\\)\\\\\\/,]+)$",
						Pattern.CASE_INSENSITIVE); // general regex used from
													// https://gist.github.com/lsauer/1312860/264ae813c2bd2c27a769d261c8c6b38da34e22fb
				Matcher matcherINCHI = patternINCHI.matcher(elements);
				if (!matcherINCHI.find()) {
					validationMessages = addToValidations(validationMessages, new ValidationMessage(
							"{SEQUENCE_ELEMENTS_CONSISTENT_WITH_ENCODING}", DataModel.Sequence.elements));
				}
			} else if (enc.equals(Encoding.NucleicAcid)) {
				Pattern patternNA = Pattern.compile("^[ATGCIUXQRYN]+$", Pattern.CASE_INSENSITIVE); // compiled from list at https://iupac.qmul.ac.uk/misc/naabb.html#p3
				Matcher matcherNA = patternNA.matcher(elements);
				if (!matcherNA.find()) {
					validationMessages = addToValidations(validationMessages, new ValidationMessage(
							"{SEQUENCE_ELEMENTS_CONSISTENT_WITH_ENCODING}", DataModel.Sequence.elements));
				}
			} else if (enc.equals(Encoding.SMILES)) {
				Pattern patternSMILES = Pattern.compile("^([^J][A-Za-z0-9@+\\-\\[\\]\\(\\)\\\\\\/%=#$]+)$",
						Pattern.CASE_INSENSITIVE); // general regex taken from https://www.biostars.org/p/13468/
				Matcher matcherSMILES = patternSMILES.matcher(elements);
				if (!matcherSMILES.find()) {
					validationMessages = addToValidations(validationMessages, new ValidationMessage(
							"{SEQUENCE_ELEMENTS_CONSISTENT_WITH_ENCODING}", DataModel.Sequence.elements));
				}
			}

		}

		return validationMessages;
	}

	public String getElements() throws SBOLGraphException{
		return IdentifiedValidator.getValidator().getPropertyAsString(this.resource, DataModel.Sequence.elements);
	}
	
	public void setElements(String elements) {
		RDFUtil.setProperty(this.resource, DataModel.Sequence.elements, elements);
	}
	
	public Encoding getEncoding() throws SBOLGraphException {
		Encoding encoding=null;
		URI encodingValue=IdentifiedValidator.getValidator().getPropertyAsURI(this.resource, DataModel.Sequence.encoding);
		if (encodingValue!=null){
			encoding=Encoding.get(encodingValue); 
		}
		return encoding;
	}
	
	public void setEncoding(Encoding encoding) {
		URI encodingURI=null;
		if (encoding!=null)
		{
			encodingURI=encoding.getUri();
		}
		RDFUtil.setProperty(this.resource, DataModel.Sequence.encoding, encodingURI);
	}

	public URI getResourceType()
	{
		return DataModel.Sequence.uri;
	}
	
}