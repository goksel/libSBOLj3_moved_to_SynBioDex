package org.sbolstandard.entity.measure;

import java.net.URI;
import org.apache.jena.datatypes.xsd.impl.XSDFloat;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.Resource;
import org.sbolstandard.entity.TopLevel;
import org.sbolstandard.util.RDFUtil;
import org.sbolstandard.util.SBOLGraphException;
import org.sbolstandard.vocabulary.MeasureDataModel;

public abstract class Unit extends TopLevel{
	
	
	private String symbol;
	private String label;
	private String comment;
	private String longComment;
		
	protected  Unit(Model model,URI uri) throws SBOLGraphException
	{
		super(model, uri);
	}
	
	protected  Unit(Resource resource)
	{
		super(resource);
	}
	
	public String getSymbol() {
		if (symbol==null)
		{
			symbol=RDFUtil.getPropertyAsString(this.resource, MeasureDataModel.Unit.symbol);
		}
		return symbol;
	}
	
	public void setSymbol(String symbol) {
		this.symbol = symbol;
		RDFUtil.setProperty(resource, MeasureDataModel.Unit.label, symbol);	
	}
	
	public String getLabel() {
		if (label==null)
		{
			label=RDFUtil.getPropertyAsString(this.resource, MeasureDataModel.Unit.label);
		}
		return label;
	}
	
	public void setLabel(String label) {
		this.label = label;
		RDFUtil.setProperty(resource, MeasureDataModel.Unit.label, label);
		if (!label.equals(getName()))
		{
			setName(label);
		}
	}
	
	@Override 
	public void setName(String name)
	{
		super.setName(name);
		if (!name.equals(getLabel()))
		{
			setLabel(name);
		}
	}
	
	@Override 
	public void setDescription(String description)
	{
		super.setDescription(description);
		if (!description.equals(getComment()))
		{
			setComment(description);
		}
	}
	
	public String getComment() {
		if (comment==null)
		{
			comment=RDFUtil.getPropertyAsString(this.resource, MeasureDataModel.Unit.comment);
		}
		return comment;
	}
	
	
	public void setComment(String comment) {
		this.comment = comment;
		RDFUtil.setProperty(resource, MeasureDataModel.Unit.comment, comment);
		if (!comment.equals(getDescription()))
		{
			setDescription(comment);
		}	
	}
	
	public String getLongComment() {
		if (longComment==null)
		{
			longComment=RDFUtil.getPropertyAsString(this.resource, MeasureDataModel.Unit.longComment);
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