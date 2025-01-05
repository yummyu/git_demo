package com.example.git_demo.utils;

import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.hibernate.validator.HibernateValidator;

public class ValidatorUtil {
    //hibernate 自定义校验
    final static ValidatorFactory validatorFactory = Validation.byProvider(HibernateValidator.class)
            .configure()
            .failFast(true)// hibernate.validator.fail_fast: true 快速失败返回模式，false 普通模式
            .buildValidatorFactory();
    final static Validator validator = validatorFactory.getValidator();

    //编写通用校验方法，返回校验错误信息
    public static <T> String validate(T t) {
        return validator.validate(t).toString();
    }

}
