package core;

import entity.Keyword;
import entity.Token;
import entity.TokenType;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.XMLWriter;
import util.CompileUtil;
import util.FileUtil;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Objects;

@Data
public class CompilationEngine {
    /**
     * @Author: ncjdjyh
     * @Date: 2019/1/5 22:19
     * @Description: 自顶向下递归语法分析器
     */
    public static final String TARGET_FIX = ".xml";
    private static CompilationEngine singleton;
    private CompileUtil compileUtil;
    private String className;
    private JackTokenizer tokenizer;
    // 抽象语法树根节点 所有的 token 写到这个文档里
    private Document astDocument;
    // 抽象语法树根节点
    private Element root;

    private CompilationEngine(JackTokenizer tokenizer) {
        this.tokenizer = tokenizer;
    }

    public static CompilationEngine getInstance(JackTokenizer tokenizer) {
        if (singleton == null) {
            singleton = new CompilationEngine(tokenizer);
            CompileUtil compileUtil = new CompileUtil(singleton);
            singleton.setCompileUtil(compileUtil);
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
            case (">"):
                return "&gt;";
            case ("<"):
                return "&lt;";
            case ("&"):
                return "&amp";
            default:
                return character;
        }
    }

    /* 编译整个类 */
    private void compileClass() {
        astDocument = DocumentHelper.createDocument();
        // class
        compileUtil.validateTokenValue("class", tokenizer.getCurrentTokenValue());
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
        compileUtil.validateTokenValue("{", tokenizer.getCurrentTokenValue());
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
        compileUtil.validateAnyKeywordType(token.getKeyword(), Keyword.STATIC, Keyword.FIELD);
        // initial classVarDec element
        var classVarDec = root.addElement("classVarDec");
        createNode(classVarDec);
        token = tokenizer.advance();
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
        compileUtil.checkTypeOrVoid(token);
        createNode(subroutineDec);
        // subroutineName
        token = tokenizer.advance();
        compileUtil.validateTokenType(token.getTokenType(), TokenType.IDENTIFIER);
        createNode(subroutineDec);
        // parameterList
        tokenizer.advance();
        compileParameterList(subroutineDec);
        // subroutineBody
        tokenizer.advance();
        compileSubroutineBody(subroutineDec);
    }

    private void compileParameterList(Element root) {
        Token token = tokenizer.getCurrentToken();
        compileUtil.validateTokenValue(token.getValue(), "(");
        createNode(root);
        tokenizer.advance();
        while (!tokenizer.getCurrentTokenValue().equals(")")) {
            // 说明至少有一个参数
            compileUtil.checkType(tokenizer.getCurrentToken());
            createNode(root);
            token = tokenizer.advance();
            compileUtil.validateTokenType(token.getTokenType(), TokenType.IDENTIFIER);
            createNode(root);
            token = tokenizer.advance();
            if (token.getValue().equals(",")) {
                // 多个参数
                createNode(root);
                tokenizer.advance();
            }
        }
        createNode(root);
    }

    /* 编译函数体 */
    private void compileSubroutineBody(Element root) {
        Token token = tokenizer.getCurrentToken();
        compileUtil.validateTokenValue("{", token.getValue());
        createNode(root);
        token = tokenizer.advance();
        if (token.getValue().equals("var")) {
            compileVarDec(root);
        }
        tokenizer.advance();
        compileStatements(root);
        token = tokenizer.advance();
        compileUtil.validateTokenValue("}", token.getValue());
        createNode(root);
    }

    private void compileStatements(Element root) {
        var statements = root.addElement("statements");
        var token = tokenizer.getCurrentToken();
        if (token.getValue().equals("}")) {
            return;
        }
        while (compileUtil.isStatement(token)) {
            compileStatement(statements);
        }
    }

    private void compileStatement(Element root) {
        var statement = root.addElement("statement");
        var token = tokenizer.getCurrentToken();
        var st = token.getKeyword();
        switch (st) {
            case DO -> compileDo(statement);
            case IF -> compileIf(statement);
            case WHILE -> compileWhile(statement);
            case LET -> compileLet(statement);
            case RETURN -> compileReturn(statement);
        }
    }

    /* 编译变量定义 */
    private void compileVarDec(Element root) {
        var classVarDec = root.addElement("varDec");
        Token token = tokenizer.advance();
        compileUtil.checkType(token);
        createNode(classVarDec);
        token = tokenizer.advance();
        compileUtil.validateTokenType(token.getTokenType(), TokenType.IDENTIFIER);
        // add elements
        token = tokenizer.advance();
        while (!StringUtils.equals(token.getValue(), ";")) {
            createNode(classVarDec);
            token = tokenizer.advance();
        }
        createNode(classVarDec);
    }

    private void compileDo(Element root) {

    }

    private void compileLet(Element root) {
    }

    private void compileWhile(Element root) {

    }

    private void compileReturn(Element root) {

    }

    private void compileIf(Element root) {
        var token = tokenizer.getCurrentToken();
        compileUtil.validateKeywordType(Keyword.IF, token.getKeyword());
        var _if = root.addElement("if");
        createNode(_if);
        token = tokenizer.advance();
        compileUtil.validateTokenValue("(", token.getValue());
        createNode(_if);
        tokenizer.advance();
        compileExpression(_if);
        token = tokenizer.advance();
        compileUtil.validateTokenValue(")", token.getValue());
        createNode(_if);
        token = tokenizer.advance();
        compileUtil.validateTokenValue("{", token.getValue());
        createNode(_if);
        tokenizer.advance();
        compileStatements(_if);
        token = tokenizer.advance();
        compileUtil.validateTokenValue("}", token.getValue());
        createNode(_if);
    }

    private void compileExpression(Element root) {
        var token = tokenizer.getCurrentToken();
        var expression = root.addElement("expression");
        compileUtil.checkTerm(token);
        var term = expression.addElement("term");
        compileTerm(term);
        token = tokenizer.advance();
        if (token.getValue().equals("(")) {
            // 有 operator
            createNode(term);
            token = tokenizer.advance();
            while (!token.getValue().equals(")")) {
                token = tokenizer.advance();
                // operator
                compileUtil.checkOperator(token);
                createNode(term);
                // term
                token = tokenizer.advance();
                compileTerm(term);
            }
        }
    }

    private void compileTerm(Element root) {

    }

    private void compileExpressionList(Element root) {

    }
}
