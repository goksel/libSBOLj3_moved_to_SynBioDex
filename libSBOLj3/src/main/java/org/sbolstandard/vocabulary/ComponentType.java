package org.sbolstandard.vocabulary;
import java.net.URI;

import org.sbolstandard.util.NameSpace;
public enum ComponentType
{
	 	DNA(NameSpace.SBO.local("0000251")), 
	 	RNA(NameSpace.SBO.local("0000250")),
	 	Protein(NameSpace.SBO.local("SBO:0000252")),
	 	SimpleChemical(NameSpace.SBO.local("SBO:0000247")),
	 	NoncovalentComplex(NameSpace.SBO.local("SBO:0000253")),
	 	FunctionalEntity(NameSpace.SBO.local("SBO:0000241"));
	 
	    private URI url;
	 
	    ComponentType(URI envUrl) {
	        this.url = envUrl;
	    }
	 
	    public URI getUrl() {
	        return url;
	    }
}


