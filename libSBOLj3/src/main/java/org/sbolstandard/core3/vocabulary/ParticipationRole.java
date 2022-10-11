package org.sbolstandard.core3.vocabulary;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.sbolstandard.core3.util.URINameSpace;

public enum ParticipationRole implements HasURI {
	Inhibitor(URINameSpace.SBO.local("0000020")),
	Inhibited(URINameSpace.SBO.local("0000642")),
	Stimulator(URINameSpace.SBO.local("0000459")),
	Stimulated(URINameSpace.SBO.local("0000643")),
	Reactant (URINameSpace.SBO.local("0000010")),
	Product(URINameSpace.SBO.local("0000011")),
	Promoter(URINameSpace.SBO.local("0000598")),
	Modifier(URINameSpace.SBO.local("0000019")),
	Modified(URINameSpace.SBO.local("0000644")),
	Template(URINameSpace.SBO.local("0000645"));
	
	private URI uri;

	ParticipationRole(URI uri) {
		this.uri = uri;
	}

	public URI getUri() {
		return uri;
	}
	
	public static Set<URI> getUris() {
		return lookup.keySet();
	}
	
	private static final Map<URI, ParticipationRole> lookup = new HashMap<>();

	static {
		for (ParticipationRole value : ParticipationRole.values()) {
			lookup.put(value.getUri(), value);
		}
	}

	public static ParticipationRole get(URI url) {
		return lookup.get(url);
	}
}