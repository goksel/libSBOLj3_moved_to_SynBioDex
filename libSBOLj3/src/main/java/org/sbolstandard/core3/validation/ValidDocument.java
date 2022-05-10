package org.sbolstandard.core3.validation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

@Target({ElementType.TYPE, ElementType.ANNOTATION_TYPE })
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = { DocumentAnnotationValidator.class })
@Documented
public @interface ValidDocument {

    String message() default "The document is not valid.";

    Class<?>[] groups() default { };

    Class<? extends Payload>[] payload() default { };
}