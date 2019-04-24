package core;

import cn.hutool.core.lang.Assert;
import entity.Keyword;
import entity.Token;
import entity.TokenType;
import org.apache.commons.lang3.StringUtils;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.XMLWriter;
import util.FileUtil;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Objects;

public class CompilationEngine {
    /**
     * @Author: ncjdjyh
     * @Date: 2019/1/5 22:19
     * @Description: 自顶向下递归语法分析器
     */
    public static final String TARGET_FIX = ".xml";
    private String className;
    private JackTokenizer tokenizer;
    // 抽象语法树根节点 所有的 token 写到这个文档里
    private Document astDocument;
    // 抽象语法树根节点
    private Element root;
    private static CompilationEngine singleton;

    private CompilationEngine(JackTokenizer tokenizer) {
        this.tokenizer = tokenizer;
    }

    public static CompilationEngine getInstance(JackTokenizer tokenizer) {
        if (singleton == null) {
            singleton = new CompilationEngine(tokenizer);
        }
        return singleton;
    }

    public static CompilationEngine getInstance() {
        return Objects.requireNonNull(singleton);
    }

    public void generateAstXML() {
        compileClass();
        printAstFile();
    }

    /* 在 element 下创建子节点 */
    public void createNode(Token token, Element element) {
        var elementName = token.getTokenType().getName();
        var elementText = token.getValue();
        elementText = exchangeXMLBuiltCharacter(elementText);
        element.addElement(elementName).setText(elementText);
    }

    /* 在 element 下创建子节点 */
    public void createNode(Element element) {
        Token token = tokenizer.getCurrentToken();
        var elementName = token.getTokenType().getName();
        var elementText = token.getValue();
        elementText = exchangeXMLBuiltCharacter(elementText);
        element.addElement(elementName).setText(elementText);
    }

    /* 在根节点下创建子节点 */
    public void compileNormal(Token token) {
        var elementName = token.getTokenType().getName();
        var elementText = token.getContent();
        root.addElement(elementName).setText(elementText);
    }

    /* 输入 XML 文件 */
    public void printAstFile() {
        var format = OutputFormat.createPrettyPrint();
        format.setEncoding("utf-8");
        var f = FileUtil.fetchExchangeSuffixFile(tokenizer.getFile(), TARGET_FIX);
        try {
            var writer = new XMLWriter(new FileWriter(f), format);
            writer.write(astDocument);
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String exchangeXMLBuiltCharacter(String character) {
        switch (character) {
            case (">") : return "&gt;";
            case ("<") : return "&lt;";
            case ("&") : return "&amp";
            default: return character;
        }
    }

    /* 编译整个类 */
    private void compileClass() {
        astDocument = DocumentHelper.createDocument();
        // class
        validateTokenValue("class", tokenizer.getCurrentTokenValue());
        root = astDocument.addElement("class");
        tokenizer.advance();
        // className
        boolean isLegalClassName = tokenizer.getCurrentTokenType() == TokenType.IDENTIFIER;
        if (!isLegalClassName) {
            throw new RuntimeException("the class name is not illegal");
        }
        this.className = tokenizer.getCurrentTokenValue();
        createNode(root);
        tokenizer.advance();
        validateTokenValue("{", tokenizer.getCurrentTokenValue());
        tokenizer.advance();
        String classBodyValue = tokenizer.getCurrentTokenValue();
        while (!classBodyValue.equals("}")) {
            if (StringUtils.equalsAny(classBodyValue, Keyword.FIELD.getName(), Keyword.STATIC.getName())) {
                // classVarDec
                compileClassVarDec();
            } else if (StringUtils.equalsAny(classBodyValue, Keyword.CONSTRUCTOR.getName(), Keyword.FUNCTION.getName(), Keyword.METHOD.getName())) {
                // subroutineDec
                compileSubroutine();
            }
            tokenizer.advance();
            classBodyValue = tokenizer.getCurrentTokenValue();
        }
    }

    /**
     * 编译静态声明或字段声明.
     */
    public void compileClassVarDec() {
        var token = tokenizer.getCurrentToken();
        // initial classVarDec element
        var classVarDec = root.addElement("classVarDec");
        // add elements
        while (!StringUtils.equals(token.getValue(), ";")) {
            createNode(classVarDec);
            token = tokenizer.advance();
        }
        createNode(classVarDec);
    }

    /*  编译整个方法、函数或构造函数 */
    private void compileSubroutine() {
        var subroutineDec = root.addElement("subroutineDec");
        createNode(subroutineDec);
        // return type
        tokenizer.advance();
        Token token = tokenizer.getCurrentToken();
        if (!isCorrectParamType(token)) {
            throw new RuntimeException("the return type is not illegal");
        }
        createNode(subroutineDec);
        // subroutineName
        token = tokenizer.advance();
        validateTokenType(token.getTokenType(), TokenType.IDENTIFIER);
        createNode(subroutineDec);
        // parameterList
        tokenizer.advance();
        compileParameterList(subroutineDec);
        // subroutineBody
        compileSubroutineBody(subroutineDec);
    }

    private void compileSubroutineBody(Element root) {
    }

    private boolean isCorrectParamType(Token token) {
        var keyword = token.getKeyword();
        return keyword == Keyword.INT
                || keyword == Keyword.CHAR
                || keyword == Keyword.BOOLEAN
                || keyword == Keyword.VOID
                || StringUtils.equals(token.getValue(), className);
    }

    private void compileStatements() {

    }

    private void compileDo() {

    }

    private void compileLet() {

    }

    private void compileWhile() {

    }

    private void compileReturn() {

    }

    private void compileIf() {

    }

    private void compileExpression() {

    }

    private void compileTerm() {

    }

    private void compileExpressionList() {

    }

    private void compileParameterList(Element root) {
        Token token = tokenizer.getCurrentToken();
        validateTokenValue(token.getValue(), "(");
        createNode(root);
        tokenizer.advance();
        while (!tokenizer.getCurrentTokenValue().equals(")")) {
            // 说明至少有一个参数
            if (isCorrectParamType(tokenizer.getCurrentToken())) {
                createNode(root);
                token = tokenizer.advance();
                validateTokenType(token.getTokenType(), TokenType.IDENTIFIER);
                createNode(root);
                token = tokenizer.advance();
                if (token.getValue().equals(",")) {
                    // 多个参数
                    createNode(root);
                    tokenizer.advance();
                }
            } else {
                throw new RuntimeException("please input a correct paramType");
            }
        }
        createNode(root);
    }

    /* 检查 token 中的值是否符合预期如果不是, 抛出一个编译时错误 */
    private void validateTokenValue(final String expected, final String actual) {
        if (!expected.equals(actual)) {
            throw new RuntimeException("Syntax expected token '" + expected
                    + "' but actual was '" + actual + "'\n"
                    + tokenizer.getCurrentToken().getContent());
        }
    }

    /* 检查 token 中的值是否符合预期如果不是, 抛出一个编译时错误 */
    private void validateTokenType(TokenType expected, TokenType actual) {
        if (!Objects.equals(expected, actual)) {
            throw new RuntimeException("Syntax expected token type '" + expected.getName()
                    + "' but actual was '" + actual.getName() + "'\n"
                    + tokenizer.getCurrentToken().getContent());
        }
    }
}
