package com.tlj.aicodegenerator.core;

import com.tlj.aicodegenerator.model.enums.CodeGenTypeEnum;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.File;
@SpringBootTest
class AiCodeGeneratorFacadeTest {
    @Resource AiCodeGeneratorFacade aiCodeGeneratorFacade;
    @Test
    void generateAndSaveCode() {
        File file = aiCodeGeneratorFacade.generateAndSaveCode("生成一个登录页面,不超过50行", CodeGenTypeEnum.HTML);
        Assertions.assertNotNull(file);
    }
}