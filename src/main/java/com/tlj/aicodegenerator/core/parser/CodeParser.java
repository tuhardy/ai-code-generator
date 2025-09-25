package com.tlj.aicodegenerator.core.parser;

/**
 * 代码解析器接口
 */
public interface CodeParser<T> {
    /**
     * 解析代码
     * @param codeContent
     * @return
     */
    T parseCode(String codeContent);
}
