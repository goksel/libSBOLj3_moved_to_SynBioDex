package org.sbolstandard.entity.test;

import java.io.IOException;
import java.net.URI;
import java.util.Arrays;
import java.util.List;

import org.sbolstandard.TestUtil;
import org.sbolstandard.api.SBOLAPI;
import org.sbolstandard.entity.Component;
import org.sbolstandard.entity.Identified;
import org.sbolstandard.entity.Metadata;
import org.sbolstandard.entity.SBOLDocument;
import org.sbolstandard.io.SBOLWriter;
import org.sbolstandard.util.SBOLGraphException;
import org.sbolstandard.util.URINameSpace;
import org.sbolstandard.vocabulary.ComponentType;
import org.sbolstandard.vocabulary.DataModel;
import org.sbolstandard.vocabulary.ModelFramework;
import org.sbolstandard.vocabulary.ModelLanguage;
import org.sbolstandard.vocabulary.Role;

import junit.framework.TestCase;

public class AnnotationTest extends TestCase {
	
	public void testImplementation() throws SBOLGraphException, IOException
    {
		String baseUri="https://sbolstandard.org/examples/";
        SBOLDocument doc=new SBOLDocument(URI.create(baseUri));
        URINameSpace igem=new URINameSpace(URI.create("http://parts.igem.org/"), "igem");
        doc.addNameSpacePrefixes(igem);
        
        Component part=SBOLAPI.createComponent(doc, SBOLAPI.append(baseUri, "BBa_J23119"), ComponentType.DNA.getUrl(), "BBa_J23119", "BBa_J23119 part", "Parts J23100 through J23119 are a family of constitutive promoter parts isolated from a small combinatorial library.", Role.Promoter);
        part.addAnnotion(igem.local("group"), "iGEM2006_Berkeley");
        part.addAnnotion(igem.local("experienceURL"), URI.create("http://parts.igem.org/Part:BBa_J23119:Experience"));
        
        Metadata igemInf=new Metadata(doc, SBOLAPI.append(part.getUri(), "information1"), igem.local("Information"));
        igemInf.setName("BBa_J23119_experience");
        igemInf.setDescription("The experience page captures users' experience using the BBa_J23119 part");
        igemInf.addAnnotion(igem.local("sigmaFactor"), "//rnap/prokaryote/ecoli/sigma70");
        igemInf.addAnnotion(igem.local("regulation"), "//regulation/constitutive");
        igemInf.addAnnotion(igem.local("regulation"), "//regulation/second_regulation");
        part.addAnnotion(igem.local("hasInformation"), igemInf);
        
        
        Metadata igemInf2=new Metadata(doc, SBOLAPI.append(part.getUri(), "usage1"), igem.local("IGEMUsage"));
        igemInf2.setName("BBa_J23119_usage");
        igemInf2.setDescription("BBa_J23119 usage statistics");
        igemInf2.addAnnotion(igem.local("inStock"), "true");
        igemInf2.addAnnotion(igem.local("registryStar"), "1");
        igemInf2.addAnnotion(igem.local("uses"), "442");
        igemInf2.addAnnotion(igem.local("twins"), "7");
        
        Metadata igemInfInternal=new Metadata(doc, SBOLAPI.append(part.getUri(), "twinParts"), igem.local("TwinPartUsage"));
        igemInfInternal.setName("twin parts");
        igemInfInternal.addAnnotion(igem.local("twinURL"), URI.create("http://parts.igem.org/wiki/index.php?title=Part:BBa_J72073"));
        igemInfInternal.addAnnotion(igem.local("twinURL"), URI.create("http://parts.igem.org/wiki/index.php?title=Part:BBa_M1638"));
        igemInfInternal.addAnnotion(igem.local("twinURL"), URI.create("http://parts.igem.org/wiki/index.php?title=Part:BBa_M36800"));

        igemInf2.addAnnotion(igem.local("twinURLs"), igemInfInternal);
        
        part.addAnnotion(igem.local("hasUsage"), igemInf2);
       
        
        Metadata igemInf4=new Metadata(doc, URI.create("http://parts.igem.org"),igem.local("Repository"),true );
        igemInf4.setName("iGEM Registry");
        igemInf4.setDescription("Registry of Standard Biological Parts");
        igemInf4.addAnnotion(igem.local("website"), "http://parts.igem.org/Main_Page");
        part.addAnnotion(igem.local("belongsTo"), igemInf4);
        
        TestUtil.serialise(doc, "entity/annotation", "annotation");
        
        String output=SBOLWriter.write(doc, "Turtle");
        System.out.println(output);
        SBOLDocument doc2=SBOLWriter.read(output, "Turtle"); 
        doc2.addTopLevelResourceType(igem.local("Repository"));
        output=SBOLWriter.write(doc2, "RDF/XML-ABBREV");
        System.out.println(output);
         
        printMetadata(doc2.getComponents().get(0));
          
    }
	
	public void printMetadata(Identified identified)
	{
		 URINameSpace igem=new URINameSpace(URI.create("http://parts.igem.org/"), "igem");  
		 System.out.println("group:" + identified.getAnnotion(igem.local("group")));
		 System.out.println("experienceURL:" + identified.getAnnotion(igem.local("experienceURL")));
		 List<Object> informationMetadata=identified.getAnnotion(igem.local("hasInformation"));
		 if (informationMetadata!=null)
		 {
			 Metadata metadata=(Metadata) informationMetadata.get(0);
			 System.out.println("hasInformation");
			 System.out.println("   uri:" + metadata.getUri());
			 System.out.println("   name:" + metadata.getName());
			 System.out.println("   desc:" + metadata.getDescription());
			 System.out.println("   SigmaFactor:" + metadata.getAnnotion(igem.local("sigmaFactor")));
			 System.out.println("   Regulation:" + metadata.getAnnotion(igem.local("regulation")));
			 
			 
			 
			 
			 
					 
			 
		 }
		 
	}
	
	/*
	public class IgemInformation extends Metadata
	{
		public IgemInformation (SBOLDocument doc, URI uri) throws SBOLGraphException
		{
			super(doc, uri);
		}
		
		@Override
		public URI getResourceType() {
			return URI.create("http://parts.igem.org/IgemInformation");
		}
	}*/

}
