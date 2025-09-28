package com.tlj.aicodegenerator.core.saver;

import cn.hutool.core.util.StrUtil;
import com.tlj.aicodegenerator.ai.model.MultiFileCodeResult;
import com.tlj.aicodegenerator.exception.BusinessException;
import com.tlj.aicodegenerator.exception.ErrorCode;
import com.tlj.aicodegenerator.model.enums.CodeGenTypeEnum;

/**
 * 多文件代码保存器
 *
 * @author yupi
 */
public class MultiFileCodeFileSaverTemplate extends CodeFileSaverTemplate<MultiFileCodeResult> {

    @Override
    public CodeGenTypeEnum getCodeType() {
        return CodeGenTypeEnum.MULTI_FILE;
    }

    @Override
    protected void saveFiles(MultiFileCodeResult result, String baseDirPath) {
        // 保存 HTML 文件
        saveCodeFile(baseDirPath, "index.html", result.getHtmlCode());
        // 保存 CSS 文件
        saveCodeFile(baseDirPath, "style.css", result.getCssCode());
        // 保存 JavaScript 文件
        saveCodeFile(baseDirPath, "script.js", result.getJsCode());
    }

    @Override
    protected void ValidateInput(MultiFileCodeResult result, Long appId) {
        super.ValidateInput(result, appId);
        // 至少要有 HTML 代码，CSS 和 JS 可以为空
        if (StrUtil.isBlank(result.getHtmlCode())) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "HTML代码内容不能为空");
        }
    }
}
