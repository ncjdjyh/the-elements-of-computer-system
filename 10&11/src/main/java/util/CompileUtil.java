package util;

import core.CompilationEngine;
import core.JackTokenizer;
import entity.Keyword;
import entity.Token;
import entity.TokenType;
import org.apache.commons.lang3.StringUtils;

import java.util.Objects;

/**
 * @Author: ncjdjyh
 * @FirstInitial: 2019/4/26
 * @Description: 编译工具类
 */
public class CompileUtil {
    private CompilationEngine engine;
    private JackTokenizer tokenizer;

    public CompileUtil(CompilationEngine engine) {
        this.engine = engine;
        this.tokenizer = engine.getTokenizer();
    }

    /* 检查 token 中的值是否符合预期如果不是, 抛出一个编译时错误 */
    public void validateTokenValue(final String expected, final String actual) {
        if (!expected.equals(actual)) {
            throw new RuntimeException("Syntax expected token '" + expected
                    + "' but actual was '" + actual + "'\n"
                    + tokenizer.getCurrentToken().getContent());
        }
    }

    /* 检查 token 中的类型是否符合预期如果不是, 抛出一个编译时错误 */
    public void validateTokenType(TokenType expected, TokenType actual) {
        if (!Objects.equals(expected, actual)) {
            throw new RuntimeException("Syntax expected token type '" + expected.getName()
                    + "' but actual was '" + actual.getName() + "'\n"
                    + tokenizer.getCurrentToken().getContent());
        }
    }

    /* 检查 token 中的关键字类型是否符合预期如果不是, 抛出一个编译时错误 */
    public void validateKeywordType(Keyword expected, Keyword actual) {
        if (!Objects.equals(expected, actual)) {
            throw new RuntimeException("Syntax expected symbol type '" + expected.getName()
                    + "' but actual was '" + actual.getName() + "'\n"
                    + tokenizer.getCurrentToken().getContent());
        }
    }

    /* 检查 token 中的关键字类型是否有任一符合预期如果不是, 抛出一个编译时错误 */
    public void validateAnyKeywordType(Keyword actual, Keyword... expected) {
        for (Keyword v : expected) {
            if (Objects.equals(actual, v)) {
                return;
            }
        }
        StringBuilder sb = new StringBuilder();
        for (Keyword v : expected) {
            sb.append(v.getName() + " ");
        }

        throw new RuntimeException("Syntax expected symbol type '" + sb.toString()
                + "' but actual was '" + actual.getName() + "'\n"
                + tokenizer.getCurrentToken().getContent());
    }

    public boolean isTypeOrVoid(Token token) {
        var keyword = token.getKeyword();
        return isType(token) || keyword == Keyword.VOID;
    }

    public void checkTypeOrVoid(Token token) {
        if (isTypeOrVoid(token) == false) {
            throw new RuntimeException("the return type is not illegal");
        }
    }

    public void checkType(Token token) {
        if (isType(token) == false) {
            throw new RuntimeException("the return type is not illegal");
        }
    }

    public boolean isType(Token token) {
        var keyword = token.getKeyword();
        return keyword == Keyword.INT
                || keyword == Keyword.CHAR
                || keyword == Keyword.BOOLEAN
                || StringUtils.equals(token.getValue(), engine.getClassName());
    }

    public void checkStatement(Token token) {
        if (isStatement(token) == false) {
            throw new RuntimeException("the statement type is not illegal");
        }
    }

    public boolean isStatement(Token token) {
        var keyword = token.getKeyword();
        return keyword == Keyword.IF ||
                keyword == Keyword.LET ||
                keyword == Keyword.DO ||
                keyword == Keyword.RETURN;
    }
}