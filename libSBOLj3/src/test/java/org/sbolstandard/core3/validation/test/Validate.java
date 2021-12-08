package org.sbolstandard.core3.validation.test;

import static org.junit.Assert.*;
import java.io.IOException;
import org.junit.Test;
import org.sbolstandard.core3.validation.SBOLValidator;

public class Validate {

	@Test
	public void validate() throws IOException {
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