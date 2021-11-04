package org.sbolstandard.core3.io;

import org.apache.jena.riot.RDFFormat;

public enum SBOLFormat
{
	 TURTLE(RDFFormat.TURTLE),
	 NTRIPLES (RDFFormat.NTRIPLES),
	 RDFXML(RDFFormat.RDFXML_ABBREV),
	 JSONLD (RDFFormat.JSONLD),
//	 JSONLD_FLAT("JSONLD_FLAT"),
	 JSONLD_EXPAND (RDFFormat.JSONLD_EXPAND_PRETTY);
//	 JSONLD_EXPAND_FLAT ("JSONLD_EXPAND_FLAT");
		
	 private RDFFormat format;
	 
	    SBOLFormat(RDFFormat format) {
	        this.format = format;
	    }
	 
	    public RDFFormat getFormat() {
	        return format;
	    }
	   
}

