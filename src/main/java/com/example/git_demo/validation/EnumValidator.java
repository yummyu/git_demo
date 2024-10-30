package com.example.git_demo.validation;


import jakarta.validation.Constraint;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import jakarta.validation.Payload;

import java.lang.annotation.*;

/**
 * 自定义枚举校验
 */
@Target({ElementType.METHOD, ElementType.FIELD, ElementType.ANNOTATION_TYPE, ElementType.CONSTRUCTOR, ElementType.PARAMETER, ElementType.TYPE_USE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Constraint(validatedBy = {EnumValidator.EnumValid.class})
public @interface EnumValidator {

    String message() default "枚举值不匹配";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    String[] enumValue();

    class EnumValid implements ConstraintValidator<EnumValidator, String> {

        private String[] enumValue;

        @Override
        public void initialize(EnumValidator constraintAnnotation) {
            this.enumValue = constraintAnnotation.enumValue();
        }

        @Override
        public boolean isValid(String value, ConstraintValidatorContext context) {
            for (String enumVal : enumValue) {
                if (enumVal.equals(value)) {
                    return true;
                }
            }
            return false;
        }

    }


}
