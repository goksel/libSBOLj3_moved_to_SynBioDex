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
	
	
	private String symbol;
	private List<String> alternativeSymbols;
	private String label;
	private List<String> alternativeLabels;
	private String comment;
	private String longComment;
		
	protected  Unit(Model model,URI uri) throws SBOLGraphException
	{
		super(model, uri);
	}
	
	protected  Unit(Resource resource) throws SBOLGraphException
	{
		super(resource);
	}
	
	public String getSymbol() throws SBOLGraphException{
		if (symbol==null)
		{
			symbol=IdentityValidator.getValidator().getPropertyAsString(this.resource, MeasureDataModel.Unit.symbol);
		}
		return symbol;
	}
	
	public void setSymbol(String symbol) {
		this.symbol = symbol;
		RDFUtil.setProperty(resource, MeasureDataModel.Unit.symbol, symbol);	
	}
	
	public List<String> getAlternativeSymbols() {
		if (alternativeSymbols==null)
		{
			alternativeSymbols=RDFUtil.getPropertiesAsStrings(this.resource, MeasureDataModel.Unit.alternativeSymbol);
		}
		return alternativeSymbols;
	}
	
	public void setAlternativeSymbols(List<String> alternativeSymbols) {
		this.alternativeSymbols = alternativeSymbols;
		RDFUtil.setPropertyAsStrings(resource, MeasureDataModel.Unit.alternativeSymbol, alternativeSymbols);	
	}
	
	
	public String getLabel() throws SBOLGraphException {
		if (label==null)
		{
			label=IdentityValidator.getValidator().getPropertyAsString(this.resource, MeasureDataModel.Unit.label);
		}
		return label;
	}
	
	public void setLabel(String label) throws SBOLGraphException{
		this.label = label;
		RDFUtil.setProperty(resource, MeasureDataModel.Unit.label, label);
		
		if (!label.equals(getName())){
			setName(label);
		}
	}
	
	public List<String> getAlternativeLabels() {
		if (alternativeLabels==null)
		{
			alternativeLabels=RDFUtil.getPropertiesAsStrings(this.resource, MeasureDataModel.Unit.alternativeLabel);
		}
		return alternativeLabels;
	}
	
	public void setAlternativeLabels(List<String> alternativeLabels) {
		this.alternativeLabels = alternativeLabels;
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
		if (comment==null)
		{
			comment=IdentityValidator.getValidator().getPropertyAsString(this.resource, MeasureDataModel.Unit.comment);
		}
		return comment;
	}
	
	
	public void setComment(String comment) throws SBOLGraphException {
		this.comment = comment;
		RDFUtil.setProperty(resource, MeasureDataModel.Unit.comment, comment);
		if (!comment.equals(getDescription()))
		{
			setDescription(comment);
		}	
	}
	
	public String getLongComment() throws SBOLGraphException {
		if (longComment==null)
		{
			longComment=IdentityValidator.getValidator().getPropertyAsString(this.resource, MeasureDataModel.Unit.longComment);
		}
		return longComment;
	}
	
	public void setLongComment(String longComment) {
		this.longComment = longComment;
		RDFUtil.setProperty(resource, MeasureDataModel.Unit.longComment, longComment);		
	}
	
	@Override
	public URI getResourceType() {
		return MeasureDataModel.Unit.uri;
	}
	
}