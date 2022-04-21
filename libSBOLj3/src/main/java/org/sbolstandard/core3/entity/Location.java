package org.sbolstandard.core3.entity;

import java.net.URI;
import java.util.Optional;
import java.util.OptionalInt;

import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.Resource;
import org.sbolstandard.core3.util.RDFUtil;
import org.sbolstandard.core3.util.SBOLGraphException;
import org.sbolstandard.core3.validation.IdentityValidator;
import org.sbolstandard.core3.validation.PropertyValidator;
import org.sbolstandard.core3.vocabulary.DataModel;
import org.sbolstandard.core3.vocabulary.Orientation;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

public abstract class  Location extends Identified {
	/*private Orientation orientation;
	private int order=Integer.MIN_VALUE;
	protected URI sequence;*/

	protected Location(Model model,URI uri) throws SBOLGraphException
	{
		super(model, uri);
	}
	
	protected  Location(Resource resource) throws SBOLGraphException
	{
		super(resource);
	}
	
	/*public Location(String displayId)
	{
		super(displayId);
	}*/

	public Orientation getOrientation() throws SBOLGraphException{
		Orientation orientation=null;
		URI value=IdentityValidator.getValidator().getPropertyAsURI(this.resource, DataModel.orientation);
		if (value!=null){
			orientation=Orientation.get(value); 
		}
		return orientation;
	}
	
	public void setOrientation(Orientation orientation) {
		URI orientationURI=null;
		if (orientation!=null)
		{
			orientationURI=orientation.getUri();
		}
		RDFUtil.setProperty(this.resource, DataModel.orientation, orientationURI);
	}
	
	
	public OptionalInt getOrder() throws SBOLGraphException {
		OptionalInt order=OptionalInt.empty();
		String value=IdentityValidator.getValidator().getPropertyAsString(this.resource, DataModel.Location.order);
		if (value!=null){
			order=OptionalInt.of(Integer.valueOf(value));
		}
		return order;
	}
	
	public void setOrder(OptionalInt order) {
		String stringValue=null;
		if (order.isPresent())
		{
			stringValue= String.valueOf(order.getAsInt());
		}
		RDFUtil.setProperty(this.resource, DataModel.Location.order, stringValue);
	}
	
	@Valid
	@NotNull(message = "{LOCATION_SEQUENCE_NOT_NULL}")
	public URI getSequence() throws SBOLGraphException {
		return IdentityValidator.getValidator().getPropertyAsURI(this.resource, DataModel.Location.sequence);
	}

	public void setSequence(@NotNull(message = "{LOCATION_SEQUENCE_NOT_NULL}") URI sequence) throws SBOLGraphException {
		PropertyValidator.getValidator().validate(this, "setSequence", new Object[] {sequence}, URI.class);
		RDFUtil.setProperty(this.resource, DataModel.Location.sequence, sequence);	
	}
	
	public URI getResourceType()
	{
		return DataModel.Location.uri;
	}
	
	public static class LocationFactory
	{
		public static Location create(Resource resource) throws SBOLGraphException
		{
			if (RDFUtil.hasType(resource.getModel(), resource, DataModel.Cut.uri))
			{
				return new Cut(resource);
			}
			else if (RDFUtil.hasType(resource.getModel(), resource, DataModel.Range.uri))
			{
				return new Range(resource);
			}
			else if (RDFUtil.hasType(resource.getModel(), resource, DataModel.EntireSequenceLocation.uri))
			{
				return new EntireSequence(resource);
			}
			else
			{
				throw new SBOLGraphException ("Could not initialise the location entity. URI:" + resource.getURI());
			}
		}
	}
	
	public static abstract class LocationBuilder
	{
		protected URI sequence;
		private Orientation orientation;
		private int order;
		public LocationBuilder(URI sequence)
		{
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
		
		abstract public Location build(Model model, URI parentUri) throws SBOLGraphException;
		abstract public URI getLocationTypeUri();
		abstract public Class getLocationClass();
	}
	
	public static class CutLocationBuilder extends LocationBuilder
	{
		private int at;
		public CutLocationBuilder(int at, URI sequence)
		{
			super(sequence);
			this.at=at;
		}
		
		public Cut build(Model model, URI uri) throws SBOLGraphException 
		{
			Cut location= new Cut(model, uri);
			location.setSequence(sequence);
			location.setAt(Optional.of(at));
			return location;
		}
		
		public URI getLocationTypeUri()
		{
			return DataModel.Cut.uri;
		}
		
		public Class getLocationClass()
		{
			return Cut.class;
		}
	}
	
	public static class RangeLocationBuilder extends LocationBuilder
	{
		private int start;
		private int end;
		
		public RangeLocationBuilder(int start, int end, URI sequence)
		{
			super(sequence);
			this.start=start;
			this.end=end;
		}
		
		public Range build(Model model, URI uri) throws SBOLGraphException
		{
			Range location= new Range(model, uri);
			location.setSequence(sequence);
			location.setStart(Optional.of(start));
			location.setEnd(Optional.of(end));
			return location;
		}
		
		public URI getLocationTypeUri()
		{
			return DataModel.Range.uri;
		}
		
		public Class getLocationClass()
		{
			return Range.class;
		}
		
	}
	
	public static class EntireSequenceLocationBuilder extends LocationBuilder
	{
		
		public EntireSequenceLocationBuilder(URI sequence)
		{
			super(sequence);
		}
		
		public EntireSequence build(Model model, URI uri) throws SBOLGraphException
		{
			EntireSequence location= new EntireSequence(model, uri);
			location.setSequence(sequence);	
			return location;
		}
		
		public URI getLocationTypeUri()
		{
			return DataModel.EntireSequenceLocation.uri;
		}
		
		public Class getLocationClass()
		{
			return EntireSequence.class;
		}
	}
	
}