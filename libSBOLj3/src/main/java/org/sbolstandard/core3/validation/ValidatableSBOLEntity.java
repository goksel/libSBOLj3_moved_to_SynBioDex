package org.sbolstandard.core3.validation;

import java.util.List;

import org.sbolstandard.core3.util.SBOLGraphException;

public interface ValidatableSBOLEntity {

	List<ValidationMessage> getValidationMessages() throws SBOLGraphException;

}
