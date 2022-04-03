package org.sbolstandard.core3.entity.measure;

import java.net.URI;
import java.util.List;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.Resource;
import org.sbolstandard.core3.entity.ControlledTopLevel;
import org.sbolstandard.core3.util.RDFUtil;
import org.sbolstandard.core3.util.SBOLGraphException;
import org.sbolstandard.core3.validation.IdentityValidator;
import org.sbolstandard.core3.vocabulary.MeasureDataModel;

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
	
	public String getSymbol() throws SBOLGraphException{
		return IdentityValidator.getValidator().getPropertyAsString(this.resource, MeasureDataModel.Unit.symbol);
	}
	
	public void setSymbol(String symbol) {
		RDFUtil.setProperty(resource, MeasureDataModel.Unit.symbol, symbol);	
	}
	
	public List<String> getAlternativeSymbols() {
		return RDFUtil.getPropertiesAsStrings(this.resource, MeasureDataModel.Unit.alternativeSymbol);
	}
	
	public void setAlternativeSymbols(List<String> alternativeSymbols) {
		RDFUtil.setPropertyAsStrings(resource, MeasureDataModel.Unit.alternativeSymbol, alternativeSymbols);	
	}
	
	
	public String getLabel() throws SBOLGraphException {
		return IdentityValidator.getValidator().getPropertyAsString(this.resource, MeasureDataModel.Unit.label);
	}
	
	public void setLabel(String label) throws SBOLGraphException{
		RDFUtil.setProperty(resource, MeasureDataModel.Unit.label, label);
		
		if (!label.equals(getName())){
			setName(label);
		}
	}
	
	public List<String> getAlternativeLabels() {
		return RDFUtil.getPropertiesAsStrings(this.resource, MeasureDataModel.Unit.alternativeLabel);
	}
	
	public void setAlternativeLabels(List<String> alternativeLabels) {
		RDFUtil.setPropertyAsStrings(resource, MeasureDataModel.Unit.alternativeLabel, alternativeLabels);
	}
	
	@Override 
	public void setName(String name)
	{
		super.setName(name);
		try
		{
			if (!name.equals(getLabel()))
			{
				setLabel(name);
			}
		}
		catch (SBOLGraphException ex)
		{
			throw new Error(ex);
		}
	}
	
	@Override 
	public void setDescription(String description)
	{
		super.setDescription(description);
		try
		{
			if (!description.equals(getComment()))
			{
				setComment(description);
			}
		}
		catch (SBOLGraphException ex)
		{
			throw new Error(ex);
		}
	}
	
	public String getComment() throws SBOLGraphException {
		return IdentityValidator.getValidator().getPropertyAsString(this.resource, MeasureDataModel.Unit.comment);
	}
	
	
	public void setComment(String comment) throws SBOLGraphException {
		RDFUtil.setProperty(resource, MeasureDataModel.Unit.comment, comment);
		if (!comment.equals(getDescription()))
		{
			setDescription(comment);
		}	
	}
	
	public String getLongComment() throws SBOLGraphException {
		return IdentityValidator.getValidator().getPropertyAsString(this.resource, MeasureDataModel.Unit.longComment);
	}
	
	public void setLongComment(String longComment) {
		RDFUtil.setProperty(resource, MeasureDataModel.Unit.longComment, longComment);		
	}
	
	@Override
	public URI getResourceType() {
		return MeasureDataModel.Unit.uri;
	}
}