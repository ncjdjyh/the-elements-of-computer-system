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
     * @Description: 自顶向下递归语法分析器 LL(0)
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
    public void createNode(Element element) {
        Token token = tokenizer.getCurrentToken();
        var elementName = token.getTokenType().getName();
        var elementText = token.getValue();
//        elementText = exchangeXMLBuiltCharacter(elementText);
        element.addElement(elementName).setText(elementText);
    }

    /* 在 element 和对应 token 下创建子节点 */
    public void createNode(Element element, Token token) {
        var elementName = token.getTokenType().getName();
        var elementText = token.getValue();
        element.addElement(elementName).setText(elementText);
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
        createNode(root);
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
        createNode(root);
        tokenizer.advance();
        String classBodyValue = tokenizer.getCurrentTokenValue();
        while (!classBodyValue.equals("}")) {
            if (StringUtils.equalsAny(classBodyValue, Keyword.FIELD.getName(), Keyword.STATIC.getName())) {
                // classVarDec
                compileClassVarDec();
            } else if (StringUtils.equalsAny(classBodyValue, Keyword.CONSTRUCTOR.getName(), Keyword.FUNCTION.getName(), Keyword.METHOD.getName())) {
                // subroutineDec
                compileSubroutineDec(root);
            }
            tokenizer.advance();
            classBodyValue = tokenizer.getCurrentTokenValue();
        }
        // create "}"
        createNode(root);
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
    private void compileSubroutineDec(Element root) {
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
        token = tokenizer.advance();
        compileUtil.validateTokenValue("(", token.getValue());
        createNode(subroutineDec);
        tokenizer.advance();
        compileParameterList(subroutineDec);
        token = tokenizer.getCurrentToken();
        compileUtil.validateTokenValue(")", token.getValue());
        createNode(subroutineDec);
        // subroutineBody
        tokenizer.advance();
        compileSubroutineBody(subroutineDec);
    }

    private void compileParameterList(Element root) {
        Element parameterList = root.addElement("parameterList").addText("");
        Token token = tokenizer.getCurrentToken();
        while (token.getValue().equals(")") == false) {
            tokenizer.back();
            // 至少有一个参数
            do {
                token = tokenizer.advance();
                compileUtil.checkType(token);
                createNode(parameterList);
                token = tokenizer.advance();
                createNode(parameterList);
                token = tokenizer.advance();
            } while (token.getValue().equals(","));
        }
    }

    /* 编译函数体 */
    private void compileSubroutineBody(Element root) {
        Element subroutineBody = root.addElement("subroutineBody");
        Token token = tokenizer.getCurrentToken();
        compileUtil.validateTokenValue("{", token.getValue());
        createNode(subroutineBody);
        token = tokenizer.advance();
        if (token.getValue().equals("}")) {
            createNode(subroutineBody);
            return;
        } else {
            while (token.getValue().equals("var")) {
                compileVarDec(subroutineBody);
                token = tokenizer.advance();
            }
            compileStatements(subroutineBody);
            token = tokenizer.getCurrentToken();
            compileUtil.validateTokenValue("}", token.getValue());
            createNode(subroutineBody);
        }
    }

    /* 编译多条语句 */
    private void compileStatements(Element root) {
        var statements = root.addElement("statements");
        var token = tokenizer.getCurrentToken();
        if (token.getValue().equals("}")) {
            return;
        }
        if (compileUtil.isStatement(token) == false) {
            throw new RuntimeException("compileStatements error: token is not a statement:" + token.getValue());
        }
        while (compileUtil.isStatement(token)) {
            compileStatement(statements);
            token = tokenizer.getCurrentToken();
        }
    }

    /* 编译特定语句 */
    private void compileStatement(Element root) {
        var token = tokenizer.getCurrentToken();
        if (token.getTokenType() != TokenType.KEYWORD) {
            token = tokenizer.advance();
            compileUtil.validateTokenValue("}", token.getValue());
            return;
        }
        var st = token.getKeyword();
        switch (st) {
            case DO -> compileDo(root);
            case IF -> compileIf(root);
            case WHILE -> compileWhile(root);
            case LET -> compileLet(root);
            case RETURN -> compileReturn(root);
        }
        tokenizer.advance();
    }

    /* 编译变量定义 */
    private void compileVarDec(Element root) {
        var classVarDec = root.addElement("varDec");
        Token token = tokenizer.getCurrentToken();
        compileUtil.validateTokenValue("var", token.getValue());
        createNode(classVarDec);
        token = tokenizer.advance();
        compileUtil.checkType(token);
        createNode(classVarDec);
        token = tokenizer.advance();
        compileUtil.validateTokenType(token.getTokenType(), TokenType.IDENTIFIER);
        createNode(classVarDec);
        // add elements
        token = tokenizer.advance();
        while (!StringUtils.equals(token.getValue(), ";")) {
            createNode(classVarDec);
            token = tokenizer.advance();
        }
        createNode(classVarDec);
    }

    private void compileDo(Element root) {
        var token = tokenizer.getCurrentToken();
        compileUtil.validateKeywordType(Keyword.DO, token.getKeyword());
        var doStatement = root.addElement("doStatement");
        createNode(doStatement);
        token = tokenizer.advance();
        compileSubroutineCall(doStatement);
        token = tokenizer.advance();
        compileUtil.validateTokenValue(";", token.getValue());
        createNode(doStatement);
    }

    private void compileLet(Element root) {
        var token = tokenizer.getCurrentToken();
        compileUtil.validateKeywordType(Keyword.LET, token.getKeyword());
        var letStatement = root.addElement("letStatement");
        createNode(letStatement);
        token = tokenizer.advance();
        compileUtil.validateTokenType(TokenType.IDENTIFIER, token.getTokenType());
        createNode(letStatement);
        token = tokenizer.peekAdvance();
        if (token.getValue().equals("[")) {
            tokenizer.advance();
            // 处理数组的形式
            createNode(letStatement);
            tokenizer.advance();
            compileExpression(letStatement);
            token = tokenizer.getCurrentToken();
            compileUtil.validateTokenValue("]", token.getValue());
            createNode(letStatement);
        }

        token = tokenizer.advance();
        compileUtil.validateTokenValue("=", token.getValue());
        createNode(letStatement);
        tokenizer.advance();
        compileExpression(letStatement);
        token = tokenizer.getCurrentToken();
        compileUtil.validateTokenValue(";", token.getValue());
        createNode(letStatement);
    }

    private void compileWhile(Element root) {
        var token = tokenizer.getCurrentToken();
        compileUtil.validateKeywordType(Keyword.WHILE, token.getKeyword());
        var whileStatement = root.addElement("whileStatement");
        createNode(whileStatement);
        token = tokenizer.advance();
        compileUtil.validateTokenValue("(", token.getValue());
        createNode(whileStatement);
        token = tokenizer.advance();
        compileExpression(whileStatement);
        token = tokenizer.getCurrentToken();
        compileUtil.validateTokenValue(")", token.getValue());
        createNode(whileStatement);
        token = tokenizer.advance();
        compileUtil.validateTokenValue("{", token.getValue());
        createNode(whileStatement);
        tokenizer.advance();
        compileStatements(whileStatement);
        token = tokenizer.getCurrentToken();
        compileUtil.validateTokenValue("}", token.getValue());
        createNode(whileStatement);
    }

    private void compileReturn(Element root) {
        var token = tokenizer.getCurrentToken();
        compileUtil.validateKeywordType(Keyword.RETURN, token.getKeyword());
        var returnStatement = root.addElement("returnStatement");
        createNode(returnStatement);
        token = tokenizer.advance();
        if (!token.getValue().equals(";")) {
            // 有 expression
            compileExpression(returnStatement);
            token = tokenizer.getCurrentToken();
        }
        compileUtil.validateTokenValue(";", token.getValue());
        createNode(returnStatement);
    }

    private void compileIf(Element root) {
        var token = tokenizer.getCurrentToken();
        compileUtil.validateKeywordType(Keyword.IF, token.getKeyword());
        var ifStatement = root.addElement("ifStatement");
        createNode(ifStatement);
        token = tokenizer.advance();
        compileUtil.validateTokenValue("(", token.getValue());
        createNode(ifStatement);
        tokenizer.advance();
        compileExpression(ifStatement);
        token = tokenizer.getCurrentToken();
        compileUtil.validateTokenValue(")", token.getValue());
        createNode(ifStatement);
        token = tokenizer.advance();
        compileUtil.validateTokenValue("{", token.getValue());
        createNode(ifStatement);
        tokenizer.advance();
        compileStatements(ifStatement);
        token = tokenizer.getCurrentToken();
        compileUtil.validateTokenValue("}", token.getValue());
        createNode(ifStatement);
        token = tokenizer.peekAdvance();
        // 有 else 语句
        if (token.getValue().equals("else")) {
            tokenizer.advance();
            var elseStatement = root.addElement("elseStatement");
            createNode(elseStatement);
            token = tokenizer.advance();
            compileUtil.validateTokenValue("{", token.getValue());
            createNode(elseStatement);
            token = tokenizer.advance();
            compileStatements(elseStatement);
            token = tokenizer.getCurrentToken();
            compileUtil.validateTokenValue("}", token.getValue());
            createNode(elseStatement);
        }
    }

    /* 编译表达式 */
    private void compileExpression(Element root) {
        var token = tokenizer.getCurrentToken();
        var expression = root.addElement("expression");
        compileTerm(expression);
        token = tokenizer.advance();
        while (compileUtil.isOperator(token)) {
            createNode(expression);
            token = tokenizer.advance();
            compileTerm(expression);
            token = tokenizer.advance();
        }
    }

    private void compileTerm(Element root) {
        var token = tokenizer.getCurrentToken();
        var term = root.addElement("term");
        var tt = token.getTokenType();
        var isConstant =
                tt == TokenType.INT_CONST ||
                tt == TokenType.STRING_CONST ||
                tt == TokenType.KEYWORD;
        if (isConstant) {
            createNode(term);
        } else if (StringUtils.equalsAnyIgnoreCase(token.getValue(), "-", "~")) {
            createNode(term);
            tokenizer.advance();
            compileTerm(term);
        } else if (StringUtils.equalsIgnoreCase(token.getValue(), "(")) {
            createNode(term);
            tokenizer.advance();
            compileExpression(term);
            token = tokenizer.getCurrentToken();
            compileUtil.validateTokenValue(")", token.getValue());
            createNode(term);
        } else {
            // 现在 tokenType 一定是 identifier
            compileUtil.validateTokenType(TokenType.IDENTIFIER, token.getTokenType());
            // 向前看一位, 分情况处理(字符 数组 调用)
            token = tokenizer.peekAdvance();
            if (StringUtils.equalsAny(token.getValue(), ".", "(")) {
                compileSubroutineCall(term);
            } else if (token.getValue().equals("[")) {
                tokenizer.advance();
                createNode(term);
                tokenizer.advance();
                compileExpression(term);
                token = tokenizer.getCurrentToken();
                compileUtil.validateTokenValue("]", token.getValue());
                createNode(term);
            } else {
                // 只有一个 varName
                createNode(term);
            }
        }
    }

    private void compileSubroutineCall(Element root) {
        var token = tokenizer.getCurrentToken();
        compileUtil.validateTokenType(TokenType.IDENTIFIER, token.getTokenType());
        createNode(root);
        token = tokenizer.advance();
        if (token.getValue().equals("(")) {
            createNode(root);
            tokenizer.advance();
            compileExpressionList(root);
            token = tokenizer.getCurrentToken();
            compileUtil.validateTokenValue(")", token.getValue());
            createNode(root);
        } else if (token.getValue().equals(".")) {
            createNode(root);
            token = tokenizer.advance();
            compileUtil.validateTokenType(TokenType.IDENTIFIER, token.getTokenType());
            createNode(root);
            token = tokenizer.advance();
            compileUtil.validateTokenValue("(", token.getValue());
            tokenizer.advance();
            compileExpressionList(root);
            token = tokenizer.getCurrentToken();
            compileUtil.validateTokenValue(")", token.getValue());
        } else {
            throw new RuntimeException("illegal subroutine arguments" + token.getValue());
        }
    }

    /* 编译子函数调用参数表, 可以有 0 个和 n 个 */
    private void compileExpressionList(Element root) {
        var token = tokenizer.getCurrentToken();
        var expressionList = root.addElement("expressionList").addText("");
        while (token.getValue().equals(")") == false) {
            // 至少一个参数
            token = tokenizer.back();
            do {
                token = tokenizer.advance();
                compileExpression(expressionList);
                token = tokenizer.getCurrentToken();
            } while (token.getValue().equals(","));
        }
    }
}
