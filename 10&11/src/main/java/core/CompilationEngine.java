package core;

import entity.BuiltInType;
import entity.Token;
import org.apache.commons.lang3.StringUtils;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.XMLWriter;
import entity.CompileFactory;
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
        createRoot();
        while (tokenizer.hasMoreTokens()) {
            tokenizer.advance();
            Token t = tokenizer.getCurrentToken();
            CompileFactory.executeCompile(t);
        }
    }

    /* 在 element 下创建子节点 */
    public void createNode(Token token, Element element) {
        var elementName = token.getTokenType().getAlias();
        var elementText = token.getContent();
        elementText = exchangeXMLBuiltCharacter(elementText);
        element.addElement(elementName).setText(elementText);
    }

    /* 在根节点下创建子节点 */
    public void compileNormal(Token token) {
        var elementName = token.getTokenType().getAlias();
        var elementText = token.getContent();
        root.addElement(elementName).setText(elementText);
    }

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

    private void createRoot() {
        astDocument = DocumentHelper.createDocument();
        root = astDocument.addElement("tokens");
    }

    public void compileClassVarDec(Token token) {
        // initial classVarDec
        var classVarDec = root.addElement("classVarDec");
        // add elements
        while (!StringUtils.equals((CharSequence) token.getValue(), ";")) {
            createNode(token, classVarDec);
            tokenizer.advance();
            token = tokenizer.getCurrentToken();
        }
        createNode(token, classVarDec);
    }

    public void compileClass(Token token) {
    }

    private void compileSubroutine() {

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
}
