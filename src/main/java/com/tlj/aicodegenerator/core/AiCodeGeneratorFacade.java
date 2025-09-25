package com.tlj.aicodegenerator.core;

import com.tlj.aicodegenerator.ai.AiCodeGeneratorService;
import com.tlj.aicodegenerator.ai.model.HtmlCodeResult;
import com.tlj.aicodegenerator.ai.model.MultiFileCodeResult;
import com.tlj.aicodegenerator.core.parser.CodeParserExecutor;
import com.tlj.aicodegenerator.core.saver.CodeFileSaverExecutor;
import com.tlj.aicodegenerator.exception.BusinessException;
import com.tlj.aicodegenerator.exception.ErrorCode;
import com.tlj.aicodegenerator.model.enums.CodeGenTypeEnum;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

import java.io.File;

/**
 * AI代码生成器门面类,组合代码生成和保存功能
 */
@Slf4j
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
            case HTML-> {
                HtmlCodeResult htmlCodeResult = aiCodeGeneratorService.generateHtmlCode(userMessage);
                // 保存代码到指定目录
                yield CodeFileSaverExecutor.executeSaver(htmlCodeResult, CodeGenTypeEnum.HTML);
            }
            case MULTI_FILE-> {
                MultiFileCodeResult multiFileCodeResult = aiCodeGeneratorService.generateMultiFileCode(userMessage);
                // 保存代码到指定目录
                yield CodeFileSaverExecutor.executeSaver(multiFileCodeResult, CodeGenTypeEnum.MULTI_FILE);
            }
            default->{
                throw  new BusinessException(ErrorCode.PARAMS_ERROR,userMessage + "不支持的生成类型" + codeGenTypeEnum.getValue());
            }
        };

    }
    /**
     * 统一入口：根据类型生成并保存代码（流式）
     *
     * @param userMessage     用户提示词
     * @param codeGenTypeEnum 生成类型
     */
    public Flux<String> generateAndSaveCodeStream(String userMessage, CodeGenTypeEnum codeGenTypeEnum) {
        if (codeGenTypeEnum == null) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "生成类型为空");
        }
        return switch (codeGenTypeEnum) {
            case HTML -> {
                Flux<String> result = aiCodeGeneratorService.generateHtmlCodeStream(userMessage);
                // 当流式返回生成代码完成后，再保存代码
                yield  ProcessCodeStream(result, CodeGenTypeEnum.HTML);
            }
            case MULTI_FILE -> {
                Flux<String> result = aiCodeGeneratorService.generateMultiFileCodeStream(userMessage);
                // 当流式返回生成代码完成后，再保存代码
                yield  ProcessCodeStream(result, CodeGenTypeEnum.MULTI_FILE);
            }
            default -> {
                String errorMessage = "不支持的生成类型：" + codeGenTypeEnum.getValue();
                throw new BusinessException(ErrorCode.SYSTEM_ERROR, errorMessage);
            }
        };
    }

    /**
     * 处理流式返回的生成代码，并保存到指定目录(通用的方法)
     * @param codeStream
     * @param codeType
     * @return
     */
    private Flux<String> ProcessCodeStream(Flux<String> codeStream, CodeGenTypeEnum codeType) {
        // 当流式返回生成代码完成后，再保存代码
        StringBuilder codeBuilder = new StringBuilder();
        // 实时收集代码片段
        return codeStream
                .doOnNext(codeBuilder::append)
                .doOnComplete(() -> {
                    // 流式返回完成后保存代码
                    try {
                        String completeMultiFileCode = codeBuilder.toString();
                        Object CodeResult = CodeParserExecutor.executeParser(completeMultiFileCode, codeType);
                        // 保存代码到文件
                        File savedDir = CodeFileSaverExecutor.executeSaver(CodeResult, codeType);
                        log.info("保存成功，路径为：" + savedDir.getAbsolutePath());
                    } catch (Exception e) {
                        log.error("保存失败: {}", e.getMessage());
                    }
                });
    }

}
