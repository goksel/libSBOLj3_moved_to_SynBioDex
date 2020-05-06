package org.sbolstandard.vocabulary;
import java.net.URI;
public enum Orientation
{
	 inline(URI.create("http://sbols.org/v3#inline")), 
	    reverseComplement(URI.create("http://sbols.org/v3#reverseComplement"));
	  
	    private URI url;
	 
	    Orientation(URI envUrl) {
	        this.url = envUrl;
	    }
	 
	    public URI getUrl() {
	        return url;
	    }
}

