package org.sbolstandard.core3.validation.test;

import static org.junit.Assert.*;
import java.io.IOException;
import org.junit.Test;
import org.sbolstandard.core3.util.SBOLGraphException;
import org.sbolstandard.core3.validation.SBOLValidator;

public class Validate {

	@Test
	public void validate() throws IOException, SBOLGraphException {
		String message=SBOLValidator.validateFolder("output","ttl");
		
		if (message!=null)
		{
			System.out.println(message);
		}
		else
		{
			fail ("Validation could not find any errors!");
		}
	}
		
	
}
