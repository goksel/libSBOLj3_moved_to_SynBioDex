package org.sbolstandard;

import java.net.URI;

import org.apache.jena.rdf.model.Resource;
import org.sbolstandard.util.RDFUtil;
import org.sbolstandard.vocabulary.DataModel;

public class SBOLEntityFactory {
	public static Identified create(Resource resource, URI entityType) throws SBOLException
	{
		if (entityType.equals(URI.create("http://sbols.org/v3#Sequence"))) {
			return new Sequence(resource);
		}
		else if (entityType.equals(URI.create("http://sbols.org/v3#Component"))) {
			return new Component(resource);
		}
		else if (entityType.equals(DataModel.Entity.Feature)) {
			if (RDFUtil.hasType(resource.getModel(), resource, DataModel.Entity.SubComponent))
			{
				return new SubComponent(resource);
			}
			else
			{
				
				String message=String.format("Could not create the SBOL feature. Entity type:%s Resource:%s", entityType.toString(),resource.getURI().toString());
				throw new SBOLException(message);
			}
		}
		else 
		{
			String message=String.format("Could not create the SBOL entity. Entity type:%s Resource:%s", entityType.toString(),resource.getURI().toString());
			throw new SBOLException(message);
		}
			
	}

}
