package com.tlj.aicodegenerator.core.saver;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import com.tlj.aicodegenerator.exception.BusinessException;
import com.tlj.aicodegenerator.exception.ErrorCode;
import com.tlj.aicodegenerator.model.enums.CodeGenTypeEnum;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.nio.charset.StandardCharsets;


/**
 * 抽象代码保存期 -模板方法模式
 * @param <T>
 */
@Slf4j
public abstract class CodeFileSaverTemplate<T> {
    private static final String FILE_SAVE_ROOT_DIR=System.getProperty("user.dir")+"/tmp/code_output";

    /**
     * 模板方法模式 - 保存代码文件的标准流程
     * @param result
     * @return
     */
    public final File saveCode(T result){
        //1.验证输入
        ValidateInput(result);
        //2.构造唯一目录
        String bashDirPath = buildUniqueDir();
        //3.保存文件（具体实现交给子类）
        saveFiles(result, bashDirPath);
        //4.返回文件目录对象
        return new File(bashDirPath);
    }

    /**
     *  验证输入参数（可由子类覆盖）
     * @param result
     */
    protected void ValidateInput(T result) {
        if(result == null){
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"保存代码失败，代码结果不能为空");
        }
    }


    /**
     *  生成代码文件路径
     */
    protected String buildUniqueDir() {
        String bizType=getCodeType().getValue();
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
    public final void saveCodeFile(String dirPath, String fileName, String content) {
        if(StrUtil.isNotBlank(content)){
            String filePath=dirPath+ File.separator+fileName;
            FileUtil.writeString(content,filePath, StandardCharsets.UTF_8);
            log.info("保存代码文件成功，文件路径："+filePath);
        }
    }

    /**保存代码文件（由子类实现）
     *
     * @param result
     * @param bashDirPath
     */
    protected abstract void saveFiles(T result, String bashDirPath);

    /**
     *  获取代码类型
     * @return
     */
    protected abstract CodeGenTypeEnum getCodeType();
}
