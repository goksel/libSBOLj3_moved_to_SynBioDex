package org.sbolstandard.entity.measure;

import java.net.URI;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.Resource;
import org.sbolstandard.util.RDFUtil;
import org.sbolstandard.util.SBOLGraphException;
import org.sbolstandard.vocabulary.MeasureDataModel;

public class PrefixedUnit extends Unit{
	
	private URI prefix;
	private URI unit;
	
	
	protected  PrefixedUnit(Model model,URI uri) throws SBOLGraphException
	{
		super(model, uri);
	}
	
	protected  PrefixedUnit(Resource resource)
	{
		super(resource);
	}
	
	public URI getPrefix() {
		if (prefix==null)
		{
			prefix=RDFUtil.getPropertyAsURI(this.resource, MeasureDataModel.PrefixedUnit.prefix);	
		}
		return prefix;
	}

	public void setPrefix(URI subject) {
		this.prefix = subject;
		RDFUtil.setProperty(resource, MeasureDataModel.PrefixedUnit.prefix, prefix);
	}
	
	
	public URI getUnit() {
		if (unit==null)
		{
			unit=RDFUtil.getPropertyAsURI(this.resource, MeasureDataModel.PrefixedUnit.unit);	
		}
		return unit;
	}

	public void setUnit(URI unit) {
		this.unit = unit;
		RDFUtil.setProperty(resource, MeasureDataModel.PrefixedUnit.unit, unit);
	}
	
	@Override
	public URI getResourceType() {
		return MeasureDataModel.PrefixedUnit.uri;
	}
	
}