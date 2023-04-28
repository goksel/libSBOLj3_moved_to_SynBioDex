package org.sbolstandard.core3.vocabulary;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.sbolstandard.core3.util.URINameSpace;

public enum ActivityType {
	Design(URINameSpace.SBOL.local("design")), 
	Build(URINameSpace.SBOL.local("build")), 
	Test(URINameSpace.SBOL.local("test")), 
	Learn(URINameSpace.SBOL.local("learn")); 
	
	private URI url;

	ActivityType(URI envUrl) {
		this.url = envUrl;
	}

	public URI getUri() {
		return url;
	}
	
	private static final Map<URI, ActivityType> lookup = new HashMap<>();
	private static final Map<ActivityType, ActivityType> lookupPreceding = new HashMap<>();
	
	    
	static{
		for(ActivityType activity: ActivityType.values()){
			lookup.put(activity.getUri(), activity);
		}

		lookupPreceding.put(Design, Learn);//Design's preceding type is Learn
		lookupPreceding.put(Build, Design);
		lookupPreceding.put(Test, Build);
		lookupPreceding.put(Learn, Test);
		

	}

	public static ActivityType get(URI uri) {
		return lookup.get(uri);
	}

	public static ActivityType getPreceding(ActivityType activity) {
		return lookupPreceding.get(activity);
	}
	
	public static Set<URI> getURIs() {
		return lookup.keySet();
	}
	    
}
