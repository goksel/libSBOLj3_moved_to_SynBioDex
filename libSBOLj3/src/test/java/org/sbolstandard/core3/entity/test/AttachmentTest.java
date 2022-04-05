package org.sbolstandard.core3.entity.test;

import java.io.IOException;
import java.net.URI;
import java.util.Arrays;
import java.util.OptionalLong;

import org.sbolstandard.core3.api.SBOLAPI;
import org.sbolstandard.core3.entity.Attachment;
import org.sbolstandard.core3.entity.Component;
import org.sbolstandard.core3.entity.Implementation;
import org.sbolstandard.core3.entity.SBOLDocument;
import org.sbolstandard.core3.io.SBOLFormat;
import org.sbolstandard.core3.io.SBOLIO;
import org.sbolstandard.core3.test.TestUtil;
import org.sbolstandard.core3.util.SBOLGraphException;
import org.sbolstandard.core3.vocabulary.ComponentType;
import org.sbolstandard.core3.vocabulary.ModelLanguage;
import org.sbolstandard.core3.vocabulary.Role;

import junit.framework.TestCase;

public class AttachmentTest extends TestCase {
	
	public void testAtatchment() throws SBOLGraphException, IOException
    {
		String baseUri="https://sbolstandard.org/examples/";
        SBOLDocument doc=new SBOLDocument(URI.create(baseUri));
        
        Component TetR_protein=SBOLAPI.createComponent(doc,"TetR_protein", ComponentType.Protein.getUrl(), "TetR", "TetR protein", Role.TF);
      
        Attachment attachment=doc.createAttachment("attachment1", URI.create("https://sbolstandard.org/attachment1"));
        attachment.setFormat(ModelLanguage.SBML);
        attachment.setSize(OptionalLong.of(1000));
        attachment.setHashAlgorithm("Alg1");
        attachment.setHash("aaa");
        
        Implementation impl=doc.createImplementation("impl1");
        impl.setComponent(TetR_protein.getUri());
        impl.setAttachments(Arrays.asList(attachment.getUri()));
        
        TestUtil.serialise(doc, "entity/attachment", "attachment");
        System.out.println(SBOLIO.write(doc, SBOLFormat.TURTLE));
        TestUtil.assertReadWrite(doc); 
        
        URI temp=attachment.getSource();
        attachment.setSource(URI.create("https://sbolstandard.org/attachment1_source2"));
        TestUtil.validateIdentified(attachment,doc,0);
        attachment.setSource(temp);
        
        //Attachment.source: exactly one.
        attachment.setSource(null);
        TestUtil.validateIdentified(attachment,doc,1);
        attachment.setSource(temp);
        
        //Attachment.source: optional
        temp=attachment.getFormat();
        attachment.setFormat(null);
        TestUtil.validateIdentified(attachment, doc,0);
        attachment.setFormat(temp);
        
        //Attachment.hashAlgorithm: optional
        String tempString=attachment.getHashAlgorithm();
        attachment.setHashAlgorithm(null);
        TestUtil.validateIdentified(attachment,doc,0);
        attachment.setHashAlgorithm(tempString);
        
        //Attachment.hash: optional
        tempString=attachment.getHash();
        attachment.setHash(null);
        TestUtil.validateIdentified(attachment,doc,0);
        attachment.setHash(tempString);
        
        //Attachment size can't be negative
        OptionalLong tempLong=attachment.getSize();
        attachment.setSize(OptionalLong.of(-1));
        TestUtil.validateIdentified(attachment,doc,1);
        
        //Attachment size can be empty
        attachment.setSize(OptionalLong.empty());
        TestUtil.validateIdentified(attachment,doc,0);
              
        //Attachment size can be zero
        attachment.setSize(OptionalLong.of(0));
        TestUtil.validateIdentified(attachment,doc,0);
        
        //Attachment size can be bigger than zero
        attachment.setSize(tempLong);
        TestUtil.validateIdentified(attachment,doc,0);
      
    }
	
	
	
	/*public  List<String> validateAttachment32(Attachment attachment)
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
	}*/

}
