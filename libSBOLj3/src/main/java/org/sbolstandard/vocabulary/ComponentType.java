package org.sbolstandard.vocabulary;
import java.net.URI;

import org.sbolstandard.util.URINameSpace;
public enum ComponentType
{
	 	DNA(URINameSpace.SBO.local("0000251")), 
	 	RNA(URINameSpace.SBO.local("0000250")),
	 	Protein(URINameSpace.SBO.local("0000252")),
	 	SimpleChemical(URINameSpace.SBO.local("0000247")),
	 	NoncovalentComplex(URINameSpace.SBO.local("0000253")),
	 	FunctionalEntity(URINameSpace.SBO.local("0000241")),
		Cell(URINameSpace.GO.local("0005623"));
	
	 
	    private URI url;
	 
	    ComponentType(URI envUrl) {
	        this.url = envUrl;
	    }
	 
	    public URI getUrl() {
	        return url;
	    }
}


