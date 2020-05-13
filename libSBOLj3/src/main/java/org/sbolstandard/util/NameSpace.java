package org.sbolstandard.util;

import java.net.URI;

public abstract class NameSpace {

	protected  URI uri;
	protected String prefix;
	protected static final String SO_Namespace="https://identifiers.org/SO:";
	protected static final String SBO_Namespace="https://identifiers.org/SBO:";
	protected static final String CHEBI_Namespace="https://identifiers.org/CHEBI:";
	protected static final String GO_Namespace="https://identifiers.org/GO:";
	protected static final String SBOL_Namespace="http://sbols.org/v3#";
	
	public URI getUri() {
		return uri;
	}
	
	public String getPrefix() {
		return prefix;
	}
	
	private NameSpace() {}
	private NameSpace(URI uriParam, String prefixParam)
	{
		this.uri=uriParam;
		this.prefix=prefixParam;	
	}
	
	public  URI local(String name)
	{
		return URI.create(uri.toString() + name);
	}
	
	public static NameSpace SO=SONameSpace.getInstance();
	public static NameSpace SBO=SBONameSpace.getInstance();
	public static NameSpace SBOL=SBOLNameSpace.getInstance();
	public static NameSpace CHEBI=CHEBINameSpace.getInstance();
	public static NameSpace GO=GONameSpace.getInstance();
	
	
	
	//protected abstract NameSpace getInstance();
	
	public static class SONameSpace extends NameSpace
	{
		private static SONameSpace instance=null;
		private SONameSpace () {}
		
		
		protected static NameSpace getInstance()
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
	
	protected static class SBONameSpace extends NameSpace
	{
		private static SBONameSpace instance=null;
		
		private SBONameSpace()
		{
			
		}
		protected static NameSpace getInstance()
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
	
	protected static class SBOLNameSpace extends NameSpace
	{
		private static SBOLNameSpace instance=null;
		
		private SBOLNameSpace()
		{
		}
		protected static NameSpace getInstance()
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
	
	protected static class CHEBINameSpace extends NameSpace
	{
		private static CHEBINameSpace instance=null;
		
		private CHEBINameSpace()
		{
		}
		protected static NameSpace getInstance()
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
	
	protected static class GONameSpace extends NameSpace
	{
		private static GONameSpace instance=null;
		
		private GONameSpace()
		{
		}
		protected static NameSpace getInstance()
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
	
}
