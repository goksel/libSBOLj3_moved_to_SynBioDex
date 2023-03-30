package org.sbolstandard.core3.entity.test;


import java.io.IOException;
import java.net.URI;
import org.sbolstandard.core3.api.SBOLAPI;
import org.sbolstandard.core3.entity.*;
import org.sbolstandard.core3.test.TestUtil;
import org.sbolstandard.core3.util.Configuration;
import org.sbolstandard.core3.util.SBOLGraphException;
import org.sbolstandard.core3.vocabulary.Role;

import junit.framework.TestCase;

public class ZInternationalURITest extends TestCase {
	
	public void testComponent() throws SBOLGraphException, IOException, Exception
    {
		//URI base=URI.create("https://synbiohub.org/public/igem/");
		URI base=URI.create("https://synbiohub.org");
		
		SBOLDocument doc=new SBOLDocument(base);
		Configuration.getInstance().setValidateAfterSettingProperties(false);
		//String pattern="[\\p{L}]+";
		String pattern="^[\\p{L}_]+[\\p{L}0-9_]*$";
		
		System.out.println("a".matches(pattern));
		System.out.println("abb".matches(pattern));
		System.out.println("abb1".matches(pattern));
		System.out.println("1abb1".matches(pattern));		
		System.out.println("abcö".matches(pattern));
		System.out.println("清华大学".matches(pattern));
		System.out.println("päypal".matches(pattern));
		System.out.println("ab#".matches(pattern));
		
		
		String[] test = {"Jean-Marie Le'Blancö", "Żółć", "Ὀδυσσεύς", "原田雅彦"};
		for (String str : test) {
		    System.out.print(str.matches("[\\p{Alpha}]+") + " ");
		   // System.out.print(str.matches("^(?U)[\\p{Alpha}\\-'. ]+") + " ");
		    
		}
		
		

		//Locale.setDefault(Locale.ENGLISH);
		//testAlphabet(Locale.ENGLISH, doc, 'a', 'z');
		//testAlphabet(new Locale("tr"), doc, 'a', 'z');
		char f='a';
		char l='я';
		//testAlphabet(new Locale("ru"), doc, f, l);
		//System.out.println(getAlphabet('a', 'я'));
		//char[] armenianAlphabet = getAlphabet(LocaleLanguage.ARMENIAN, true);
		
		//char[] russianAlphabet = getAlphabet(true,'а','я');
		
		//doc.createComponent(base, base, Arrays.asList((ComponentType.DNA.getUri())));
		TestUtil.validateDocument(doc, 0);
		
		testComponent(doc, "1abb", 1);
		
		//testComponent(doc, "1abb", 1);
		
		
	    testComponent(doc, "abb",0);
		testComponent(doc, "abcö", 0);
		testComponent(doc, "ab1_AB123", 0);
		testComponent(doc, "_ab", 0);
		testComponent(doc, "_test", 0);
		testComponent(doc, "ab_12", 0);
		
		char[] frChars = {'a','b','c','d','e','f','g','h','i','j','k','l','m','n','o','p','q','r','s','t','u','v','w','x','y','z','A','B','C','D','E','F','G','H','I','J','K','L','M','N','O','P','Q','R','S','T','U','V','W','X','Y','Z','é','è','à','ù','ç','â','ê','î','ô','û','ë','ï'};
 		String strFrenchIdentifier= String.valueOf(frChars);
 		testComponent(doc, strFrenchIdentifier, 0);
 		testComponent(doc, strFrenchIdentifier.toUpperCase(), 0); 		
 		testComponent(doc, "АБВГДЕЖЗИЙКЛМНОПРСТУФХЦЧШЩЪЫЬЭЮЯ", 0);//Russian?
 		
 		
 		char[] trChars = {'a','b','c','d','e','f','g','h','i','j','k','l','m','n','o','p','r','s','t','u','v','x','y','z','ç','ğ','ö','ı','ş','ü'};
 		String strTurkishIdentifier= String.valueOf(trChars);
 		testComponent(doc, strTurkishIdentifier, 0);
 		testComponent(doc, strTurkishIdentifier.toUpperCase(), 0); 		
 	
 		testComponent(doc, "päypal", 0);
 		
 		testComponent(doc, "清华大学", 0);
 		
 		testComponent(doc, "ÀÈÌÒÙàèìòùÁÉÍÓÚÝáéíóúýÂÊÎÔÛâêîôûÃÑÕãñõÄËÏÖÜäëïöüiçÇßØøÅåÆæÞþÐð",0);
 	  //testComponent(doc, "ÀÈÌÒÙàèìòùÁÉÍÓÚÝáéíóúýÂÊÎÔÛâêîôûÃÑÕãñõÄËÏÖÜäëïöü¡¿çÇßØøÅåÆæÞþÐð",0);
 		
 		testComponent(doc, "päypal1", 0);
 		testComponent(doc, "£päypal", 1); 	 	 	
 		testComponent(doc, "1päypal", 1);
		testComponent(doc, "ab1-AB", 1);
		testComponent(doc, "ab.", 1);		
		try {
			testComponent(doc, "ab#", 1);
		}
		catch (Exception ex){
        	if (!(ex instanceof SBOLGraphException)){
        		throw ex;
        	}
		}
		
		try {
			testComponent(doc, "ab?", 1);
		}
		catch (Exception ex){
        	if (!(ex instanceof SBOLGraphException)){
        		throw ex;
        	}
		}        
		testComponent(doc, "ab'", 1);		
		
    }
	
	private void testComponent(SBOLDocument doc, String displayId, int error) throws SBOLGraphException
	{
		Component c6=SBOLAPI.createDnaComponent(doc, displayId, null, null, Role.Promoter, null); 
		TestUtil.validateIdentified(c6,error);	
	}
	
	
	
	private char[] getAlphabet(boolean flagToUpperCase,char firstLetter, char lastLetter) {
	    /*if (localeLanguage == null)
	        localeLanguage = LocaleLanguage.ENGLISH;*/

	   // char firstLetter = 'а'; //localeLanguage.getFirstLetter();
	   // char lastLetter = 'я';//localeLanguage.getLastLetter();
	    int alphabetSize = lastLetter - firstLetter + 1;

	    char[] alphabet = new char[alphabetSize];

	    for (int index = 0; index < alphabetSize; index++) {
	        alphabet[index] = (char) (index + firstLetter);
	    }

	    if (flagToUpperCase) {
	        alphabet = new String(alphabet).toUpperCase().toCharArray();
	    }

	    return alphabet;
	}

	
}
