package org.sbolstandard.core3.entity.test;

import java.io.IOException;
import java.net.URI;
import java.util.OptionalLong;
import org.sbolstandard.core3.entity.Attachment;
import org.sbolstandard.core3.entity.SBOLDocument;
import org.sbolstandard.core3.test.TestUtil;
import org.sbolstandard.core3.util.SBOLGraphException;
import org.sbolstandard.core3.vocabulary.HashAlgorithm;
import org.sbolstandard.core3.vocabulary.ModelLanguage;
import junit.framework.TestCase;

public class AttachmentTest_12803 extends TestCase {
	
	public void testAttachment() throws SBOLGraphException, IOException, Exception
    {
		String baseUri="https://sbolstandard.org/examples/";
        SBOLDocument doc=new SBOLDocument(URI.create(baseUri));
        
        Attachment attachment2=doc.createAttachment("attachment2", URI.create("https://sbolstandard.org/attachment2"));
        attachment2.setFormat(ModelLanguage.SBML);
        attachment2.setSize(OptionalLong.of(1000));
        attachment2.setHashAlgorithm(HashAlgorithm.blake2b_256);
        attachment2.setHash("aaa");
        
        TestUtil.validateIdentified(attachment2,doc,0);
        
        attachment2.setFormat(URI.create("http://invalidformat.org"));		
     	TestUtil.validateIdentified(attachment2,doc,1,"sbol3-12803");
     	attachment2.setFormat(ModelLanguage.CellML);		
     	TestUtil.validateIdentified(attachment2,doc,0);     	
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
