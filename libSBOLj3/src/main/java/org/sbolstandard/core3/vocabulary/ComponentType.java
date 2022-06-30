package org.sbolstandard.core3.vocabulary;
import java.net.URI;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.sbolstandard.core3.util.URINameSpace;
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
	    
	    private static final Map<URI, ComponentType> lookup = new HashMap<>();
	    
		static {
			for(ComponentType value : ComponentType.values())
	        {
	            lookup.put(value.getUrl(), value);
	        }
		}
		
		private static final Map<URI, ComponentType> recommendedLookup = new HashMap<>();
	    
		static {
			recommendedLookup.put(DNA.getUrl(), DNA);
			recommendedLookup.put(RNA.getUrl(), RNA);
			recommendedLookup.put(Protein.getUrl(), Protein);
			recommendedLookup.put(SimpleChemical.getUrl(), SimpleChemical);
			recommendedLookup.put(NoncovalentComplex.getUrl(), NoncovalentComplex);
			recommendedLookup.put(FunctionalEntity.getUrl(), FunctionalEntity);
		}
		  
	    public static ComponentType get(URI url) 
	    {
	        return lookup.get(url);
	    }
	    
	    public static ComponentType getRecommendedType(URI url) 
	    {
	        return recommendedLookup.get(url);
	    }
	    
	    // hardcoded list of matches from table 2 to table 1
	    public static List<URI> checkComponentTypeMatch(ComponentType type){
	    	switch(type) {
	    	case DNA:
	    		return Arrays.asList(Encoding.NucleicAcid.getUri());
	    	case RNA:
	    		return Arrays.asList(Encoding.NucleicAcid.getUri());
	    	case Protein:
	    		return Arrays.asList(Encoding.AminoAcid.getUri());
	    	case SimpleChemical:
	    		return Arrays.asList(Encoding.INCHI.getUri(),Encoding.SMILES.getUri());
	    	default:
	    		return null;
	    	}
	    }
	    
}


