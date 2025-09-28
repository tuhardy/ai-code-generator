package com.tlj.aicodegenerator.core.saver;

import cn.hutool.core.util.StrUtil;
import com.tlj.aicodegenerator.ai.model.HtmlCodeResult;
import com.tlj.aicodegenerator.exception.BusinessException;
import com.tlj.aicodegenerator.exception.ErrorCode;
import com.tlj.aicodegenerator.model.enums.CodeGenTypeEnum;

public class HtmlCodeFileSaverTemplate extends CodeFileSaverTemplate<HtmlCodeResult> {

    @Override
    protected CodeGenTypeEnum getCodeType() {
        return CodeGenTypeEnum.HTML;
    }
    @Override
    protected void saveFiles(HtmlCodeResult result, String bashDirPath) {
        saveCodeFile(bashDirPath, "index.html", result.getHtmlCode());
    }

    @Override
    protected void ValidateInput(HtmlCodeResult result,Long appId) {
        super.ValidateInput(result,appId);
        //校验HTML代码是否为空
        if(StrUtil.isBlank(result.getHtmlCode())){
            throw new BusinessException(ErrorCode.SYSTEM_ERROR,"HTML代码不能为空");
        }
    }


}
