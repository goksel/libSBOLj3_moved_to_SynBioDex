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
@Constraint(validatedBy = { IdentifiedAnnotationValidator.class })
@Documented
public @interface ValidIdentified {

    String message() default "default identified message";

    Class<?>[] groups() default { };

    Class<? extends Payload>[] payload() default { };
}