package org.sbolstandard.core3.validation.test;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;
import org.junit.Test;
import org.sbolstandard.core3.util.SBOLGraphException;

public class RoundTripTest_SBOLTestSuite {

	private static final String sboltestsuite="SBOLTestSuite" + File.separator + "SBOL3"; 
	//TODO: To open later after the Test files are updated.
	//@Test
	public void validate() throws IOException, SBOLGraphException {
		
		String message=RoundTripTest.validateFolder(sboltestsuite);
		
		if (!message.isEmpty())
		{
			System.out.println(message);
			if (message.contains("Could'not"))
			{
				fail ("Validation found errors!");		
			}
		}
		

	}
	
	
}
