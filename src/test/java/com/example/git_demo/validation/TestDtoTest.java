package com.example.git_demo.validation;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.validator.HibernateValidator;
import org.junit.jupiter.api.Test;

import java.util.Set;

@Slf4j
class TestDtoTest {

    @Test
    public void testEnumValidator() throws Exception{
        TestDto testDto = new TestDto();
//        testDto.setProvince("上海");
        testDto.setProvince("安徽");
        testDto.setName("test");

        /*ValidatorFactory validatorFactory = Validation.byProvider(HibernateValidator.class)
                .configure()
                // 快速失败模式
                .failFast(true)
                .buildValidatorFactory();*/

        Validator validator =  Validation.buildDefaultValidatorFactory().getValidator();
        Set<ConstraintViolation<TestDto>> validate = validator.validate(testDto);

        log.info("{}",validate);


    }

}