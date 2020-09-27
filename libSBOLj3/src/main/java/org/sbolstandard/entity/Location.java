package org.sbolstandard.entity;

import java.net.URI;

import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.Resource;
import org.sbolstandard.util.RDFUtil;
import org.sbolstandard.util.SBOLGraphException;
import org.sbolstandard.vocabulary.DataModel;
import org.sbolstandard.vocabulary.Orientation;

public abstract class  Location extends Identified {
	private Orientation orientation;
	private int order=Integer.MIN_VALUE;
	protected URI sequence;

	protected Location(Model model,URI uri) throws SBOLGraphException
	{
		super(model, uri);
	}
	
	protected  Location(Resource resource)
	{
		super(resource);
	}
	
	public Location(URI uri)
	{
		super(uri);
	}

	public Orientation getOrientation() {
		if (orientation==null)
		{
			URI value=RDFUtil.getPropertyAsURI(this.resource, DataModel.orientation);
			if (value!=null)
			{
				orientation=Orientation.get(value); 
			}
		}
		return orientation;
	}
	
	public void setOrientation(Orientation orientation) {
		this.orientation = orientation;
		RDFUtil.setProperty(this.resource, DataModel.orientation, this.orientation.getUri());
	}
	
	
	public int getOrder() {
		if (order==Integer.MIN_VALUE)
		{
			String value=RDFUtil.getPropertyAsString(this.resource, DataModel.Location.order);
			if (value!=null)
			{
				order=Integer.valueOf(value);
			}
		}
		return order;
	}
	
	public void setOrder(int order) {
		this.order = order;
		RDFUtil.setProperty(this.resource, DataModel.Location.order, String.valueOf(this.order));
	}
	
	
	public URI getSequence() {
		if (sequence==null)
		{
			sequence=RDFUtil.getPropertyAsURI(this.resource, DataModel.Location.sequence);
		}
		return sequence;
	}

	public void setSequence(URI sequence) {
		this.sequence = sequence;
		RDFUtil.setProperty(this.resource, DataModel.Location.sequence, this.sequence);	
	}
	
	public URI getResourceType()
	{
		return DataModel.Location.uri;
	}
	
	public static class LocationFactory
	{
		public static Location create(Resource resource)
		{
			if (RDFUtil.hasType(resource.getModel(), resource, DataModel.Cut.uri))
			{
				return new CutLocation(resource);
			}
			else
			{
				return null;
			}
		}
	}
	
	public static abstract class LocationBuilder
	{
		protected URI uri;
		protected URI sequence;
		private Orientation orientation;
		private int order;
		public LocationBuilder(URI uri, URI sequence)
		{
			this.uri=uri;
			this.sequence=sequence;
		}
		
		public int getOrder()
		{
			return order;
		}
		public void setOrder(int order)
		{
			this.order=order;
		}
		

		public Orientation getOrientation()
		{
			return orientation;
		}
		public void setOrientation(Orientation orientation)
		{
			this.orientation=orientation;
		}
		
		abstract public Location build(Model model) throws SBOLGraphException;
		
	}
	
	public static class CutLocationBuilder extends LocationBuilder
	{
		private int at;
		public CutLocationBuilder(URI uri, int at, URI sequence)
		{
			super(uri,sequence);
			this.at=at;
		}
		
		public CutLocation build(Model model) throws SBOLGraphException 
		{
			CutLocation location= new CutLocation(model, this.uri);
			location.setSequence(sequence);
			location.setAt(at);
			return location;
		}
	}
	
	public static class RangeLocationBuilder extends LocationBuilder
	{
		private int start;
		private int end;
		
		public RangeLocationBuilder(URI uri, int start, int end, URI sequence)
		{
			super(uri,sequence);
			this.start=start;
			this.end=end;
			
			
		}
		
		public RangeLocation build(Model model) throws SBOLGraphException
		{
			RangeLocation location= new RangeLocation(model, this.uri);
			location.setSequence(sequence);
			location.setStart(start);
			location.setEnd(end);
			return location;
		}
	}
	
	public static class EntireSequenceLocationBuilder extends LocationBuilder
	{
		
		public EntireSequenceLocationBuilder(URI uri, URI sequence)
		{
			super(uri,sequence);
		}
		
		public EntireSequenceLocation build(Model model) throws SBOLGraphException
		{
			EntireSequenceLocation location= new EntireSequenceLocation(model, this.uri);
			location.setSequence(sequence);	
			return location;
		}
	}
	
}