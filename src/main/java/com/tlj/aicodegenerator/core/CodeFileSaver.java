package com.tlj.aicodegenerator.core;


import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import com.tlj.aicodegenerator.ai.model.HtmlCodeResult;
import com.tlj.aicodegenerator.ai.model.MultiFileCodeResult;
import com.tlj.aicodegenerator.model.enums.CodeGenTypeEnum;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.nio.charset.StandardCharsets;
@Slf4j
@Deprecated
public class CodeFileSaver {
    private static final String FILE_SAVE_ROOT_DIR=System.getProperty("user.dir")+"/tmp/code_output";

    //2.保存HTML代码
    public static File saveHtmlFile(HtmlCodeResult htmlCodeResult) {
        String baseDirPath=buildUniqueDir(CodeGenTypeEnum.HTML.getValue());
        saveCodeFile(baseDirPath,"index.html",htmlCodeResult.getHtmlCode());
        return new File(baseDirPath);
    }
    //3.保存多个代码
    public static File saveMultiFile(MultiFileCodeResult multiFileCodeResult) {
        String baseDirPath=buildUniqueDir(CodeGenTypeEnum.MULTI_FILE.getValue());
        saveCodeFile(baseDirPath,"index.html",multiFileCodeResult.getHtmlCode());
        saveCodeFile(baseDirPath,"index.html",multiFileCodeResult.getDescription());
        saveCodeFile(baseDirPath,"script.js",multiFileCodeResult.getJsCode());
        return new File(baseDirPath);
    }
    //生成代码文件路径
    public static String buildUniqueDir(String bizType) {
        String uniqueDirName = StrUtil.format("{}_{}", bizType, IdUtil.getSnowflakeNextIdStr());
        String dirPath = FILE_SAVE_ROOT_DIR + File.separator + uniqueDirName;
        FileUtil.mkdir(dirPath);
        return dirPath;
    }
    /**
     * 保存代码文件到指定目录
     * @param dirPath 文件目录
     * @param fileName 文件名称
     * @param content 代码内容
     */
    private static void saveCodeFile(String dirPath, String fileName, String content) {
        String filePath=dirPath+ File.separator+fileName;
        FileUtil.writeString(content,filePath, StandardCharsets.UTF_8);
        log.info("保存代码文件成功，文件路径："+filePath);
    }
}
