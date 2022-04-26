package org.sbolstandard.core3.vocabulary;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;

import org.sbolstandard.core3.util.URINameSpace;

import jakarta.validation.constraints.NotNull;

public enum RoleIntegration {
	overrideRoles(URINameSpace.SBOL.local("overrideRoles")), 
	mergeRoles(URINameSpace.SBOL.local("mergeRoles"));

	private URI uri;

	RoleIntegration(URI uri) {
		this.uri = uri;
	}

	public URI getUri() {
		return uri;
	}
	
	private static final Map<URI, RoleIntegration> lookup = new HashMap<>();
	  
    static
    {
        for(RoleIntegration value : RoleIntegration.values())
        {
            lookup.put(value.getUri(), value);
        }
    }
  
    public static RoleIntegration get(URI uri) 
    {
        return lookup.get(uri);
    }

}
