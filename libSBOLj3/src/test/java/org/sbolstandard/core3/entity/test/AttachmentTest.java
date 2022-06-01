package org.sbolstandard.core3.entity.test;

import java.io.IOException;
import java.net.URI;
import java.util.Arrays;
import java.util.OptionalLong;

import org.apache.jena.riot.RDFFormat;
import org.sbolstandard.core3.api.SBOLAPI;
import org.sbolstandard.core3.entity.Attachment;
import org.sbolstandard.core3.entity.Component;
import org.sbolstandard.core3.entity.Implementation;
import org.sbolstandard.core3.entity.SBOLDocument;
import org.sbolstandard.core3.io.SBOLFormat;
import org.sbolstandard.core3.io.SBOLIO;
import org.sbolstandard.core3.test.TestUtil;
import org.sbolstandard.core3.util.Configuration;
import org.sbolstandard.core3.util.SBOLGraphException;
import org.sbolstandard.core3.validation.SBOLValidator;
import org.sbolstandard.core3.vocabulary.ComponentType;
import org.sbolstandard.core3.vocabulary.ModelLanguage;
import org.sbolstandard.core3.vocabulary.Role;

import junit.framework.TestCase;

public class AttachmentTest extends TestCase {
	
	public void testAttachment() throws SBOLGraphException, IOException, Exception
    {
		String baseUri="https://sbolstandard.org/examples/";
        SBOLDocument doc=new SBOLDocument(URI.create(baseUri));
        
        Attachment attachment2=doc.createAttachment("attachment2", URI.create("https://sbolstandard.org/attachment2"));
        attachment2.setFormat(ModelLanguage.SBML);
        attachment2.setSize(OptionalLong.of(1000));
        attachment2.setHashAlgorithm("HashAlg");
        attachment2.setHash("aaa");
        System.out.println(SBOLIO.write(doc, RDFFormat.TURTLE));
        System.out.println(SBOLValidator.getValidator().isValid(doc));
        
        
        
        Component TetR_protein=SBOLAPI.createComponent(doc,"TetR_protein", ComponentType.Protein.getUrl(), "TetR", "TetR protein", Role.TF);
      
        Attachment attachment=doc.createAttachment("attachment1", URI.create("https://sbolstandard.org/attachment1"));
        attachment.setFormat(ModelLanguage.SBML);
        attachment.setSize(OptionalLong.of(1000));
        attachment.setHashAlgorithm("Alg1");
        attachment.setHash("aaa");
        
        Implementation impl=doc.createImplementation("impl1");
        impl.setComponent(TetR_protein);
        impl.setAttachments(Arrays.asList(attachment));
        
        TestUtil.serialise(doc, "entity/attachment", "attachment");
        System.out.println(SBOLIO.write(doc, SBOLFormat.TURTLE));
        TestUtil.assertReadWrite(doc);
        
        Configuration.getConfiguration().setValidateAfterSettingProperties(false);
        
        URI temp=attachment.getSource();
        attachment.setSource(URI.create("https://sbolstandard.org/attachment1_source2"));
        TestUtil.validateIdentified(attachment,doc,0);
        attachment.setSource(temp);
        
        //Attachment.source: exactly one.
        
        TestUtil.validateProperty(attachment, "setSource", new Object[] {null}, URI.class);
        attachment.setSource(null);
        TestUtil.validateIdentified(attachment,doc,1);
        attachment.setSource(temp);
        
        //Attachment.format: optional
        temp=attachment.getFormat();
        attachment.setFormat(null);
        TestUtil.validateIdentified(attachment, doc,0);
        attachment.setFormat(temp);
        
        //Attachment.hashAlgorithm: optional
        String tempString=attachment.getHashAlgorithm();
        String tempHash=attachment.getHash();
        attachment.setHashAlgorithm(null);
        attachment.setHash(null);
        TestUtil.validateIdentified(attachment,doc,0);
        attachment.setHashAlgorithm(tempString);
        attachment.setHash(tempHash);
        
        //Attachment.hash: optional
        tempString=attachment.getHash();
        attachment.setHash(null);
        TestUtil.validateIdentified(attachment,doc,0);
        attachment.setHash(tempString);
        
        //Attachment size can't be negative
        OptionalLong tempLong=attachment.getSize();
        TestUtil.validateProperty(attachment, "setSize", new Object[] {OptionalLong.of(-1)}, OptionalLong.class);
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
        
        attachment.setHashAlgorithm(null);
        TestUtil.validateIdentified(attachment,doc,1);
        
        /*attachment2.setWasDerivedFrom(null);
        TestUtil.validateIdentified(attachment2,doc,0);
        attachment2.setWasDerivedFrom(Arrays.asList(attachment2.getUri()));
        TestUtil.validateIdentified(attachment2,doc,1);*/
          
        
      
        
      
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
