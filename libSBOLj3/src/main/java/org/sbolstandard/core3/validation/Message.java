package org.sbolstandard.core3.validation;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class Message {
//public final static String ATTACHMENT_SOURCE_NOT_NULL="Attachment.source cannot be null - setSource";
public final static String test = null;// get("ATTACHMENT_SOURCE_NOT_NULL");
public final static String ATTACHMENT_SOURCE_NOT_NULL=test;//get("ATTACHMENT_SOURCE_NOT_NULL");
//private static Properties prop=null;
private static Properties prop=new Properties();//


	private Message()
	{
		try {	
			InputStream input = new FileInputStream("validation/messages");		
            prop = new Properties();
            prop.load(input);
		}
		catch (IOException ex) {
			throw new Error(ex);
        }
	}
	public static String get(String messageID)
	{
		return prop.getProperty(messageID);
	}
}
