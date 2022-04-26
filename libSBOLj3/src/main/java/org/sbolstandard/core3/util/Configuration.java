package org.sbolstandard.core3.util;


public class Configuration {
	private static Configuration configuration = null;
	
	private boolean enforceOneToOneRelationships=true;
	private boolean validateAfterSettingProperties=true;
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

	private boolean validateBeforeSaving=true;
	private boolean validateAfterReadingSBOLDocuments=true;
	
	
	
	
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
	
	/*public enum PropertyValidationType{
		ValidateBeforeSavingSBOLDocuments,
		ValidateAfterSettingProperties,
		NoValidation
	}*/
}
