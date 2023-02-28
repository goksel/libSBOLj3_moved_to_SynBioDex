package org.sbolstandard.core3.util;

import java.io.FileNotFoundException;
import java.net.URI;
import java.util.Set;

import org.apache.jena.rdf.model.Model;
import org.apache.jena.riot.Lang;
import org.apache.jena.riot.RDFFormat;
import org.sbolstandard.core3.vocabulary.Encoding;
import org.sbolstandard.core3.vocabulary.Role;

public class Configuration {
	private static Configuration configuration = null;
	
	private boolean enforceOneToOneRelationships=true;
	private boolean validateAfterSettingProperties=true;
	private boolean validateBeforeSaving=true;
	private boolean validateAfterReadingSBOLDocuments=true;
	private boolean validateRecommendedRules=true;

	
	public boolean isValidateAfterSettingProperties() {
		return validateAfterSettingProperties;
	}

	public void setValidateAfterSettingProperties(boolean validateAfterSettingProperties) {
		this.validateAfterSettingProperties = validateAfterSettingProperties;
	}

	public boolean isValidateBeforeSaving() {
		return validateBeforeSaving;
	}

	/**
	 * Set this parameter to enforce validating SBOL graphs before writing to the disk.
	 * @param validateBeforeSaving
	 */
	public void setValidateBeforeSaving(boolean validateBeforeSaving) {
		this.validateBeforeSaving = validateBeforeSaving;
	}

	public boolean isValidateRecommendedRules() {
		return validateRecommendedRules;
	}

	public void setValidateRecommendedRules(boolean validateRecommendedRules) {
		this.validateRecommendedRules = validateRecommendedRules;
	}

	
	public boolean validateAfterReadingSBOLDocuments() {
		return validateAfterReadingSBOLDocuments;
	}

	public void setValidateAfterReadingSBOLDocuments(boolean validateAfterReadingSBOLDocuments) {
		this.validateAfterReadingSBOLDocuments = validateAfterReadingSBOLDocuments;
	}

	public boolean enforceOneToOneRelationships() {
		return this.enforceOneToOneRelationships;
	}

	/***
	 * When set to true, it will enforce one-to-one relationships for SBOL graphs that are generated by other tools. For performance reasons,
	 * if the graphs are created by libSBOLj, then this property can be set to false.
	 * @param enforceOneToOneRelationships
	 */
	public void setEnforceOneToOneRelationships(boolean enforceOneToOneRelationships) {
		this.enforceOneToOneRelationships = enforceOneToOneRelationships;
	}

	//private Model edamOntology=null;
	//private Model soOntology=null;
	//private Model sboOntology=null;
	
	private Set<String> SboOccurringEntityInteractionTypes=null;
	private Set<String> SoSequenceFeatures=null;
	private Set<String> EdamEncodingTerms=null;
	private Set<String> SboModelFrameworkTerms=null;
	private Set<String> EdamFileFormatTerms=null;
	
	private Configuration()
	{
		try
		{
			Model edamOntology=SBOLUtil.getModelFromFileResource("edam.owl.reduced", RDFFormat.TURTLE);
			Model soOntology=SBOLUtil.getModelFromFileResource("so-simple.owl.reduced", RDFFormat.TURTLE);
			Model sboOntology=SBOLUtil.getModelFromFileResource("sbo.owl.reduced", RDFFormat.TURTLE);
			this.SboOccurringEntityInteractionTypes=RDFUtil.childResourcesRecursively(sboOntology,URINameSpace.SBO.local("0000231").toString());
			this.SoSequenceFeatures=RDFUtil.childResourcesRecursively(soOntology,Role.SequenceFeature.toString());
			this.EdamEncodingTerms= RDFUtil.childResourcesRecursively(edamOntology,Encoding.PARENT_TERM.toString());
			this.SboModelFrameworkTerms= RDFUtil.childResourcesRecursively(sboOntology, URINameSpace.SBO.local("0000004").toString());				
			this.EdamFileFormatTerms= RDFUtil.childResourcesRecursively(edamOntology,URINameSpace.EDAM.local("format_1915").toString());
		}
		catch (FileNotFoundException ex)
		{
			throw new Error(ex);
		}
	}
	
	private static class SingletonHelper  {
        private static final Configuration INSTANCE = new Configuration();
    }
	
	public static Configuration getInstance()
	{
		return SingletonHelper.INSTANCE;
	}
	
	
	public Set<String> getSboOccurringEntityInteractionTypes()
	{
		return this.SboOccurringEntityInteractionTypes;   
	}
	
	public Set<String> getSoSequenceFeatures()
	{
		return this.SoSequenceFeatures;   
	}
	
	public Set<String> getEdamEncodingTerms()
	{
		return this.EdamEncodingTerms;   
	}
	
	public Set<String> getEdamModelLanguageTerms()
	{
		return this.EdamFileFormatTerms;   
	}
	
	public Set<String> getSboModelFrameworkTerms()
	{
		return this.SboModelFrameworkTerms;   
	}
	
	public Set<String> getEdamFileFormatTerms()
	{
		return this.EdamFileFormatTerms;   
	}
	
	/*public static Configuration getConfiguration()
	{
		if (configuration == null)
		{
			configuration=new Configuration();
		}
		return configuration;
	}
	*/

	
	/*public enum PropertyValidationType{
		ValidateBeforeSavingSBOLDocuments,
		ValidateAfterSettingProperties,
		NoValidation
	}*/
}
