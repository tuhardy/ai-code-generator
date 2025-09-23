package com.tlj.aicodegenerator.model.enums;

import cn.hutool.core.util.ObjUtil;
import lombok.Getter;

@Getter
public enum CodeGenTypeEnum {
    HTML("原生HTML模式","html"),
    MULTI_FILE("原生多文件模式","multi_file");
    private final String text;
    private final String value;
    CodeGenTypeEnum(String text, String value) {
        this.text = text;
        this.value = value;
    }
    public static CodeGenTypeEnum getEnumByValue(String value) {
        if(ObjUtil.isEmpty(value)){
            return null;
        }
        for(CodeGenTypeEnum codeGenTypeEnum : CodeGenTypeEnum.values()){
            if(value.equals(codeGenTypeEnum.value)){
                return codeGenTypeEnum;
            }
        }
        return null;
    }
}
