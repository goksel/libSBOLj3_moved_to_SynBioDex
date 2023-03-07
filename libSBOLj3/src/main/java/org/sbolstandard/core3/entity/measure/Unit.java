package org.sbolstandard.core3.entity.measure;

import java.net.URI;
import java.util.HashMap;
import java.util.List;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.Resource;
import org.sbolstandard.core3.entity.ComponentReference;
import org.sbolstandard.core3.entity.ControlledTopLevel;
import org.sbolstandard.core3.entity.ExternallyDefined;
import org.sbolstandard.core3.entity.Identified;
import org.sbolstandard.core3.entity.LocalSubComponent;
import org.sbolstandard.core3.entity.SequenceFeature;
import org.sbolstandard.core3.entity.SubComponent;
import org.sbolstandard.core3.util.Configuration;
import org.sbolstandard.core3.util.RDFUtil;
import org.sbolstandard.core3.util.SBOLGraphException;
import org.sbolstandard.core3.validation.IdentifiedValidator;
import org.sbolstandard.core3.validation.PropertyValidator;
import org.sbolstandard.core3.validation.ValidationMessage;
import org.sbolstandard.core3.vocabulary.DataModel;
import org.sbolstandard.core3.vocabulary.MeasureDataModel;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public abstract class Unit extends ControlledTopLevel{
	
	
	/*private String symbol;
	private List<String> alternativeSymbols;
	private String label;
	private List<String> alternativeLabels;
	private String comment;
	private String longComment;*/
		
	protected  Unit(Model model,URI uri) throws SBOLGraphException
	{
		super(model, uri);
	}
	
	protected  Unit(Resource resource) throws SBOLGraphException
	{
		super(resource);
	}
	
	@NotEmpty(message = "{UNIT_SYMBOL_NOT_EMPTY}")	
	public String getSymbol() throws SBOLGraphException{
		return IdentifiedValidator.getValidator().getPropertyAsString(this.resource, MeasureDataModel.Unit.symbol);
	}
	
	public void setSymbol(@NotEmpty(message = "{UNIT_SYMBOL_NOT_EMPTY}") String symbol) throws SBOLGraphException {
		PropertyValidator.getValidator().validate(this, "setSymbol", new Object[] {symbol}, String.class);
		RDFUtil.setProperty(resource, MeasureDataModel.Unit.symbol, symbol);	
	}
	
	public List<String> getAlternativeSymbols() {
		return RDFUtil.getLiteralPropertiesAsStrings(this.resource, MeasureDataModel.Unit.alternativeSymbol);
	}
	
	public void setAlternativeSymbols(List<String> alternativeSymbols) {
		RDFUtil.setPropertyAsStrings(resource, MeasureDataModel.Unit.alternativeSymbol, alternativeSymbols);	
	}
	
	@NotEmpty(message = "{UNIT_LABEL_NOT_EMPTY}")	
	public String getLabel() throws SBOLGraphException {
		return IdentifiedValidator.getValidator().getPropertyAsString(this.resource, MeasureDataModel.Unit.label);
	}
	
	public void setLabel(@NotEmpty(message = "{UNIT_LABEL_NOT_EMPTY}") String label) throws SBOLGraphException{
		PropertyValidator.getValidator().validate(this, "setLabel", new Object[] {label}, String.class);
		RDFUtil.setProperty(resource, MeasureDataModel.Unit.label, label);
		
		if (label!=null && !label.isEmpty() && !label.equals(getName())){
			setName(label);
		}
	}
	
	public List<String> getAlternativeLabels() {
		return RDFUtil.getLiteralPropertiesAsStrings(this.resource, MeasureDataModel.Unit.alternativeLabel);
	}
	
	public void setAlternativeLabels(List<String> alternativeLabels) {
		RDFUtil.setPropertyAsStrings(resource, MeasureDataModel.Unit.alternativeLabel, alternativeLabels);
	}
	
	@Override 
	public void setName(String name) throws SBOLGraphException
	{
		super.setName(name);
		
		if (name!=null && !name.equals(getLabel()))
		{
			setLabel(name);
		}
	}
	
	@Override 
	public void setDescription(String description) throws SBOLGraphException
	{
		super.setDescription(description);
		
		if (description!=null && !description.equals(getComment()))
		{
			setComment(description);
		}
	}
	
	public String getComment() throws SBOLGraphException {
		return IdentifiedValidator.getValidator().getPropertyAsString(this.resource, MeasureDataModel.Unit.comment);
	}
	
	
	public void setComment(String comment) throws SBOLGraphException {
		RDFUtil.setProperty(resource, MeasureDataModel.Unit.comment, comment);
		if (comment!=null && !comment.equals(getDescription()))
		{
			setDescription(comment);
		}	
	}
	
	public String getLongComment() throws SBOLGraphException {
		return IdentifiedValidator.getValidator().getPropertyAsString(this.resource, MeasureDataModel.Unit.longComment);
	}
	
	public void setLongComment(String longComment) {
		RDFUtil.setProperty(resource, MeasureDataModel.Unit.longComment, longComment);		
	}
	
	@Override
	public URI getResourceType() {
		return MeasureDataModel.Unit.uri;
	}
	
	@SuppressWarnings("unchecked")
	public static <T extends Identified> HashMap<URI, Class<T>> getSubClassTypes()
	{
		HashMap<URI, Class<T>> subclasses=new HashMap<URI, Class<T>>();
		subclasses.put(MeasureDataModel.BinaryPrefix.uri, (Class<T>) BinaryPrefix.class);
		subclasses.put(MeasureDataModel.PrefixedUnit.uri, (Class<T>) PrefixedUnit.class);
		subclasses.put(MeasureDataModel.SingularUnit.uri, (Class<T>) SingularUnit.class);
		subclasses.put(MeasureDataModel.SIPrefix.uri, (Class<T>) SIPrefix.class);
		subclasses.put(MeasureDataModel.UnitDivision.uri, (Class<T>) UnitDivision.class);
		subclasses.put(MeasureDataModel.UnitExponentiation.uri, (Class<T>) UnitExponentiation.class);
		subclasses.put(MeasureDataModel.UnitMultiplication.uri, (Class<T>) UnitMultiplication.class);	
		return subclasses;
	}
	
	@Override
	public List<ValidationMessage> getValidationMessages() throws SBOLGraphException
	{
		List<ValidationMessage> validationMessages=super.getValidationMessages();
		validationMessages=assertNameLabelEqual(validationMessages);
		validationMessages=assertDescriptionCommentEqual(validationMessages);		
		return validationMessages;
	}
	
	public List<ValidationMessage> assertNameLabelEqual(List<ValidationMessage> validationMessages) throws SBOLGraphException{
		if (Configuration.getInstance().isValidateRecommendedRules()) {
			String label=this.getLabel();
			String name=this.getName();
			validationMessages=IdentifiedValidator.assertTwoPropertyValueIdenticalEqual (validationMessages, getNameLabelMessage(), name, label, DataModel.Identified.name, MeasureDataModel.Unit.label); 
			/*if (label!=null && !label.isEmpty() && name!=null && !name.isEmpty()) {
				if (!label.equals(name)){
					String message=String.format("{UNIT_NAME_LABEL_EQUAL}%sLabel: %s",ValidationMessage.INFORMATION_SEPARATOR, label);				
					ValidationMessage valMessage=new ValidationMessage(message, DataModel.Identified.name, name);  				
					validationMessages= IdentifiedValidator.addToValidations(validationMessages,valMessage);											
				}
			}*/
		}
		return validationMessages;
	}
	
	public List<ValidationMessage> assertDescriptionCommentEqual(List<ValidationMessage> validationMessages) throws SBOLGraphException {		
		if (Configuration.getInstance().isValidateRecommendedRules()) {
			String desc=this.getDescription();
			String comment=this.getComment();
			validationMessages=IdentifiedValidator.assertTwoPropertyValueIdenticalEqual (validationMessages,getDescriptionCommentMessage() , desc, comment, DataModel.Identified.description, MeasureDataModel.Unit.comment); 
		}
		return validationMessages;
	}
	
	protected String getNameLabelMessage()
	{
		return "{UNIT_NAME_LABEL_EQUAL}";
	}
	
	protected String getDescriptionCommentMessage()
	{
		return "{UNIT_DESCRIPTION_COMMENT_EQUAL}";
	}
	
	
}