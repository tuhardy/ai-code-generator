package com.tlj.aicodegenerator;

import cn.hutool.core.lang.Dict;
import cn.hutool.setting.yaml.YamlUtil;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class AiCodeGenerateApplicationTests {

    @Test
    void contextLoads() {
        Dict dict = YamlUtil.loadByPath("application.yaml");
        String url = dict.getByPath("spring.datasource.url");
        String username = dict.getByPath("spring.datasource.username");
        Integer password = dict.getByPath("spring.datasource.password");
        String passwordStr = String.valueOf(password);
        System.out.println(url);
        System.out.println(username);
        System.out.println(passwordStr);
    }

}
