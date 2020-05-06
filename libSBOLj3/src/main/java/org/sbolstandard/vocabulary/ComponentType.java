package org.sbolstandard.vocabulary;
import java.net.URI;
public enum ComponentType
{
	 	DNA(URI.create("https://identifiers.org/SBO:0000251")), 
	 	RNA(URI.create("https://identifiers.org/SBO:0000250")),
	 	Protein(URI.create("https://identifiers.org/SBO:0000252")),
	 	SimpleChemical(URI.create("https://identifiers.org/SBO:0000247")),
	 	NoncovalentComplex(URI.create("https://identifiers.org/SBO:0000253")),
	 	FunctionalEntity(URI.create("https://identifiers.org/SBO:0000241"));
	 
	    private URI url;
	 
	    ComponentType(URI envUrl) {
	        this.url = envUrl;
	    }
	 
	    public URI getUrl() {
	        return url;
	    }
}


