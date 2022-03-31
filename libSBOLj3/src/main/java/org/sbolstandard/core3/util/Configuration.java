package org.sbolstandard.core3.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URI;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.apache.commons.compress.utils.FileNameUtils;
import org.apache.commons.io.IOUtils;
import org.apache.jena.query.QuerySolution;
import org.apache.jena.query.ResultSet;
import org.apache.jena.query.Syntax;
import org.apache.jena.rdf.model.RDFNode;
import org.sbolstandard.core3.api.SBOLAPI;
import org.sbolstandard.core3.entity.Identified;
import org.sbolstandard.core3.entity.SBOLDocument;
import org.sbolstandard.core3.io.SBOLIO;
import org.sbolstandard.core3.util.RDFUtil;
import org.sbolstandard.core3.validation.IdentityValidator;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;


public class Configuration {
	private static Configuration configuration = null;
	
	private boolean enforceOneToOneRelationships=true;
	public boolean enforceOneToOneRelationships() {
		return enforceOneToOneRelationships;
	}

	public void setEnforceOneToOneRelationships(boolean enforceOneToOneRelationships) {
		this.enforceOneToOneRelationships = enforceOneToOneRelationships;
	}

	private Configuration()
	{
		
	}
	
	public static Configuration getConfiguration()
	{
		if (configuration == null)
		{
			configuration=new Configuration();
		}
		return configuration;
	}

}
