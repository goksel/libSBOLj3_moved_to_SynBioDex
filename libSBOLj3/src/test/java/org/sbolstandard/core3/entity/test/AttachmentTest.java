package org.sbolstandard.core3.entity.test;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.OptionalLong;
import java.util.Set;

import org.sbolstandard.core3.api.SBOLAPI;
import org.sbolstandard.core3.entity.Attachment;
import org.sbolstandard.core3.entity.Component;
import org.sbolstandard.core3.entity.Identified;
import org.sbolstandard.core3.entity.Implementation;
import org.sbolstandard.core3.entity.SBOLDocument;
import org.sbolstandard.core3.io.SBOLFormat;
import org.sbolstandard.core3.io.SBOLIO;
import org.sbolstandard.core3.test.TestUtil;
import org.sbolstandard.core3.util.SBOLGraphException;
import org.sbolstandard.core3.validation.IdentityValidator;
import org.sbolstandard.core3.validation.SBOLValidator;
import org.sbolstandard.core3.vocabulary.ComponentType;
import org.sbolstandard.core3.vocabulary.ModelLanguage;
import org.sbolstandard.core3.vocabulary.Role;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import junit.framework.TestCase;

public class AttachmentTest extends TestCase {
	
	public void testImplementation() throws SBOLGraphException, IOException
    {
		String baseUri="https://sbolstandard.org/examples/";
        SBOLDocument doc=new SBOLDocument(URI.create(baseUri));
        
        Component TetR_protein=SBOLAPI.createComponent(doc,"TetR_protein", ComponentType.Protein.getUrl(), "TetR", "TetR protein", Role.TF);
      
        
        Attachment attachment=doc.createAttachment("attachment1", URI.create("https://sbolstandard.org/attachment1"));
        attachment.setFormat(ModelLanguage.SBML);
        attachment.setSize(OptionalLong.of(1000));
        attachment.setHashAlgorithm("Alg1");
        attachment.setHash("aaa");
        System.out.println("Entity hash:" + attachment.getHash());
        System.out.println("Doc hash:" + doc.getAttachments().get(0).getHash());
        attachment.setHash("bbb");
        System.out.println("Entity hash:" + attachment.getHash());
        System.out.println("Doc hash:" + doc.getAttachments().get(0).getHash());
         
        
        
        
        Implementation impl=doc.createImplementation("impl1");
        impl.setComponent(TetR_protein.getUri());
        impl.setAttachments(Arrays.asList(attachment.getUri()));
        
        
        URI temp=attachment.getSource();
        attachment.setSource(URI.create("https://sbolstandard.org/attachment1_source2"));
        validateIdentified(attachment,0);
        attachment.setSource(temp);
        
        attachment.setSource(null);
        validateIdentified(attachment,1);
        attachment.setSource(temp);
        
        
        temp=attachment.getFormat();
        attachment.setFormat(null);
        validateIdentified(attachment,0);
        attachment.setFormat(temp);
        
        String tempString=attachment.getHashAlgorithm();
        attachment.setHashAlgorithm(null);
        validateIdentified(attachment,0);
        attachment.setHashAlgorithm(tempString);
        
        tempString=attachment.getHash();
        attachment.setHash(null);
        validateIdentified(attachment,0);
        attachment.setHash(tempString);
        
        OptionalLong tempLong=attachment.getSize();
        attachment.setSize(OptionalLong.of(-1));
        validateIdentified(attachment,1);
        TestUtil.assertReadWrite(doc);
        attachment.setSize(tempLong);
        TestUtil.assertReadWrite(doc);
         
        
        TestUtil.serialise(doc, "entity/attachment", "attachment");
        System.out.println(SBOLIO.write(doc, SBOLFormat.TURTLE));
        TestUtil.assertReadWrite(doc);
        
        
        
        //messages=SBOLValidator.validateSBOLDocument(doc);
      
        //messages=validateAttachment(attachment);
        //assertEquals(3, messages.size());
        

        
    }
	
	private void printMessages(List<String> messages)
	{	 
        for (String message: messages){
        	System.out.println(message);
        }
        System.out.println("--------------------");
	}
	
	private void validateIdentified(Identified identified,int numberOfExpectedErrors)
	{	 
		 List<String> messages=IdentityValidator.getValidator().validate(identified);
	     assertEquals(numberOfExpectedErrors, messages.size());
	     printMessages(messages);
	}
	public  List<String> validateAttachment(Attachment attachment)
	{
		Validator validator=null; 
		ValidatorFactory factory = Validation.byDefaultProvider()
 	            .configure()
 	            //.addValueExtractor(new ProfileValueExtractor())
 	            .buildValidatorFactory();
 	        validator = factory.getValidator();
    
 	       Set<ConstraintViolation<Attachment>> violations = validator.validate(attachment);
 	       List<String> messages=new ArrayList<String>();
 	       for (ConstraintViolation<Attachment> violation : violations) {
 	    	    String propertyMessage=String.format("Property:%s",violation.getPropertyPath().toString());
 	    	    String entityMessage =String.format("Entity:%s",violation.getRootBeanClass().getName());
 	    	    String message=String.format("%s, %s, %s", violation.getMessage(), propertyMessage, entityMessage);
 	    	    messages.add(message);
 	    	}
 	       return messages;
	}

}
