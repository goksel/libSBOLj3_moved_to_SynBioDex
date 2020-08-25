package org.sbolstandard.util;

import java.net.URI;

public class URINameSpace {

	protected  URI uri;
	protected String prefix;
	protected static final String SO_Namespace="https://identifiers.org/SO:";
	protected static final String SBO_Namespace="https://identifiers.org/SBO:";
	protected static final String CHEBI_Namespace="https://identifiers.org/CHEBI:";
	protected static final String GO_Namespace="https://identifiers.org/GO:";
	protected static final String SBOL_Namespace="http://sbols.org/v3#";
	protected static final String EDAM_Namespace="https://identifiers.org/edam:";
	//protected static final String PROV_Namespace="https://www.w3.org/TR/prov-o/";
	protected static final String PROV_Namespace="http://www.w3.org/ns/prov#";
	protected static final String OM_Namespace="http://www.ontology-of-units-of-measure.org/resource/om-2/";
	protected static final String RDFS_Namespace="http://www.w3.org/2000/01/rdf-schema#";
	
	public URI getUri() {
		return uri;
	}
	
	public String getPrefix() {
		return prefix;
	}
	
	private URINameSpace() {}
	public URINameSpace(URI uriParam, String prefixParam)
	{
		this.uri=uriParam;
		this.prefix=prefixParam;	
	}
	
	public  URI local(String name)
	{
		return URI.create(uri.toString() + name);
	}
	
	public static URINameSpace SO=SONameSpace.getInstance();
	public static URINameSpace SBO=SBONameSpace.getInstance();
	public static URINameSpace SBOL=SBOLNameSpace.getInstance();
	public static URINameSpace CHEBI=CHEBINameSpace.getInstance();
	public static URINameSpace GO=GONameSpace.getInstance();
	public static URINameSpace EDAM=EDAMNameSpace.getInstance();
	public static URINameSpace PROV=PROVNameSpace.getInstance();
	public static URINameSpace OM=OMNameSpace.getInstance();
	public static URINameSpace RDFS=OMNameSpace.getInstance();
	
	
	
	
	
	
	//protected abstract NameSpace getInstance();
	
	public static class SONameSpace extends URINameSpace
	{
		private static SONameSpace instance=null;
		private SONameSpace () {}
		
		
		protected static URINameSpace getInstance()
		{
			if (instance == null)     
			{
				instance = new SONameSpace ();
				instance.uri=URI.create(SO_Namespace);
				instance.prefix="SO";
			}
			return instance;
			
		}
	}
	
	protected static class SBONameSpace extends URINameSpace
	{
		private static SBONameSpace instance=null;
		
		private SBONameSpace()
		{
			
		}
		protected static URINameSpace getInstance()
		{
			if (instance == null)     
			{
				instance = new SBONameSpace ();
				instance.uri=URI.create(SBO_Namespace);
				instance.prefix="SBO";
			}
			return instance;
			
		}
	}
	
	protected static class SBOLNameSpace extends URINameSpace
	{
		private static SBOLNameSpace instance=null;
		
		private SBOLNameSpace()
		{
		}
		protected static URINameSpace getInstance()
		{
			if (instance == null)     
			{
				instance = new SBOLNameSpace ();
				instance.uri=URI.create(SBOL_Namespace);
				instance.prefix="sbol";
			}
			return instance;
			
		}
	}
	
	protected static class CHEBINameSpace extends URINameSpace
	{
		private static CHEBINameSpace instance=null;
		
		private CHEBINameSpace()
		{
		}
		protected static URINameSpace getInstance()
		{
			if (instance == null)     
			{
				instance = new CHEBINameSpace ();
				instance.uri=URI.create(CHEBI_Namespace);
				instance.prefix="CHEBI";
			}
			return instance;
			
		}
	}
	
	protected static class GONameSpace extends URINameSpace
	{
		private static GONameSpace instance=null;
		
		private GONameSpace()
		{
		}
		protected static URINameSpace getInstance()
		{
			if (instance == null)     
			{
				instance = new GONameSpace ();
				instance.uri=URI.create(GO_Namespace);
				instance.prefix="GO";
			}
			return instance;
			
		}
	}
	
	protected static class EDAMNameSpace extends URINameSpace
	{
		private static EDAMNameSpace instance=null;
		
		private EDAMNameSpace()
		{
		}
		protected static URINameSpace getInstance()
		{
			if (instance == null)     
			{
				instance = new EDAMNameSpace ();
				instance.uri=URI.create(EDAM_Namespace);
				instance.prefix="EDAM";
			}
			return instance;
		}
	}
	

	protected static class PROVNameSpace extends URINameSpace
	{
		private static PROVNameSpace instance=null;
		
		private PROVNameSpace()
		{
		}
		protected static URINameSpace getInstance()
		{
			if (instance == null)     
			{
				instance = new PROVNameSpace ();
				instance.uri=URI.create(PROV_Namespace);
				instance.prefix="prov";
			}
			return instance;
		}
	}
	
	protected static class OMNameSpace extends URINameSpace
	{
		private static OMNameSpace instance=null;
		
		private OMNameSpace()
		{
		}
		protected static URINameSpace getInstance()
		{
			if (instance == null)     
			{
				instance = new OMNameSpace ();
				instance.uri=URI.create(OM_Namespace);
				instance.prefix="om";
			}
			return instance;
		}
	}
	
	protected static class RDFSNameSpace extends URINameSpace
	{
		private static RDFSNameSpace instance=null;
		
		private RDFSNameSpace()
		{
		}
		protected static URINameSpace getInstance()
		{
			if (instance == null)     
			{
				instance = new RDFSNameSpace ();
				instance.uri=URI.create(URINameSpace.RDFS_Namespace);
				instance.prefix="rdfs";
			}
			return instance;
		}
	}
	
}
