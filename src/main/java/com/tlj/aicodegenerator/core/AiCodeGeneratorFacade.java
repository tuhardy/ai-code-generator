package com.tlj.aicodegenerator.core;

import com.tlj.aicodegenerator.ai.AiCodeGeneratorService;
import com.tlj.aicodegenerator.ai.model.HtmlCodeResult;
import com.tlj.aicodegenerator.ai.model.MultiFileCodeResult;
import com.tlj.aicodegenerator.exception.BusinessException;
import com.tlj.aicodegenerator.exception.ErrorCode;
import com.tlj.aicodegenerator.model.enums.CodeGenTypeEnum;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.io.File;

/**
 * AI代码生成器门面类,组合代码生成和保存功能
 */
@Service
public class AiCodeGeneratorFacade {
    @Resource
    private AiCodeGeneratorService aiCodeGeneratorService;

    /**
     * 统一入口，根据类型生成代码并保存到指定目录
     * @param userMessage
     * @param codeGenTypeEnum
     * @return
     */
    public File generateAndSaveCode(String userMessage, CodeGenTypeEnum codeGenTypeEnum) {
        if(codeGenTypeEnum==null){
            throw  new BusinessException(ErrorCode.PARAMS_ERROR,"生成类型不能为空");
        }
         return switch (codeGenTypeEnum){
            case HTML-> generateAndSaveHtmlCode(userMessage);
            case MULTI_FILE-> generateAndSaveMultiFileCode(userMessage);
            default->{
                throw  new BusinessException(ErrorCode.PARAMS_ERROR,userMessage + "不支持的生成类型" + codeGenTypeEnum.getValue());
            }
        };

    }

    /**
     * 生成多文件代码并保存到指定目录
     * @param userMessage
     * @return
     */
    private File generateAndSaveMultiFileCode(String userMessage) {
       MultiFileCodeResult multiFileCodeResult = aiCodeGeneratorService.generateMultiFileCode(userMessage);
        // 保存代码到指定目录
        return CodeFileSaver.saveMultiFile(multiFileCodeResult);
    }
    /**
     * 生成HTML代码并保存到指定目录
     * @param userMessage
     * @return
     */
    private File generateAndSaveHtmlCode(String userMessage) {
        HtmlCodeResult htmlCodeResult = aiCodeGeneratorService.generateHtmlCode(userMessage);
        // 保存代码到指定目录
        return CodeFileSaver.saveHtmlFile(htmlCodeResult);
    }
}
