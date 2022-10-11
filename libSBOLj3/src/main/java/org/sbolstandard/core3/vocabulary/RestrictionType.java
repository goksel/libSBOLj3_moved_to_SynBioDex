package org.sbolstandard.core3.vocabulary;

import java.net.URI;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.sbolstandard.core3.util.URINameSpace;

public class RestrictionType {
	
	public interface ConstraintRestriction extends HasURI
	{
		
	}
	
	public enum OrientationRestriction implements ConstraintRestriction
	{
		sameOrientationAs(URINameSpace.SBOL.local("sameOrientationAs")),
		oppositeOrientationAs(URINameSpace.SBOL.local("oppositeOrientationAs"));
		
		private URI uri;

		OrientationRestriction(URI uri) {
			this.uri = uri;
		}

		public URI getUri() {
			return uri;
		}
		
		public static Set<URI> getUris() {
			return lookup.keySet();
		}
		
		private static final Map<URI, OrientationRestriction> lookup = new HashMap<>();

		static {
			for (OrientationRestriction value : OrientationRestriction.values()) {
				lookup.put(value.getUri(), value);
			}
		}
		
		public static OrientationRestriction get(URI url) {
			return lookup.get(url);
		}
	}
	
	public enum IdentityRestriction implements ConstraintRestriction
	{
		verifyIdentical(URINameSpace.SBOL.local("verifyIdentical")),
		differentFrom(URINameSpace.SBOL.local("differentFrom")),
		replaces(URINameSpace.SBOL.local("replaces"));
		
		private URI uri;

		IdentityRestriction(URI uri) {
			this.uri = uri;
		}

		public URI getUri() {
			return uri;
		}
		
		public static Set<URI> getUris() {
			return lookup.keySet();
		}
		
		private static final Map<URI, IdentityRestriction> lookup = new HashMap<>();

		static {
			for (IdentityRestriction value : IdentityRestriction.values()) {
				lookup.put(value.getUri(), value);
			}
		}
		
		public static IdentityRestriction get(URI url) {
			return lookup.get(url);
		}
	}
	
	public enum TopologyRestriction implements ConstraintRestriction
	{
		isDisjointFrom(URINameSpace.SBOL.local("isDisjointFrom")),
		strictlyContains(URINameSpace.SBOL.local("strictlyContains")),
		contains(URINameSpace.SBOL.local("contains")),
		equals(URINameSpace.SBOL.local("equals")),
		meets(URINameSpace.SBOL.local("meets")),
		covers(URINameSpace.SBOL.local("covers")),
		overlaps(URINameSpace.SBOL.local("overlaps"));
		
		private URI uri;

		TopologyRestriction(URI uri) {
			this.uri = uri;
		}

		public URI getUri() {
			return uri;
		}
		
		public static Set<URI> getUris() {
			return lookup.keySet();
		}
		
		private static final Map<URI, TopologyRestriction> lookup = new HashMap<>();

		static {
			for (TopologyRestriction value : TopologyRestriction.values()) {
				lookup.put(value.getUri(), value);
			}
		}
		
		public static TopologyRestriction get(URI url) {
			return lookup.get(url);
		}
	}
	
	public enum SequentialRestriction implements ConstraintRestriction
	{
		precedes(URINameSpace.SBOL.local("precedes")),
		strictlyPrecedes(URINameSpace.SBOL.local("strictlyPrecedes")),
		meets(URINameSpace.SBOL.local("meets")),
		overlaps(URINameSpace.SBOL.local("overlaps")),
		contains(URINameSpace.SBOL.local("contains")),
		strictlyContains(URINameSpace.SBOL.local("strictlyContains")),
		equals(URINameSpace.SBOL.local("equals")),
		finishes(URINameSpace.SBOL.local("finishes")),	
		starts(URINameSpace.SBOL.local("starts"));	
		
		private URI uri;

		SequentialRestriction(URI uri) {
			this.uri = uri;
		}

		public URI getUri() {
			return uri;
		}
		
		public static Set<URI> getUris() {
			return lookup.keySet();
		}
		
		private static final Map<URI, SequentialRestriction> lookup = new HashMap<>();

		static {
			for (SequentialRestriction value : SequentialRestriction.values()) {
				lookup.put(value.getUri(), value);
			}
		}
		
		public static SequentialRestriction get(URI url) {
			return lookup.get(url);
		}
	}	
	
	public static Set<URI> getUris()
	{
		Set<URI> all=new HashSet<URI>();
		all.addAll(OrientationRestriction.getUris());
		all.addAll(IdentityRestriction.getUris());
		all.addAll(TopologyRestriction.getUris());
		all.addAll(SequentialRestriction.getUris());
		return all;	
	}	
	
}

