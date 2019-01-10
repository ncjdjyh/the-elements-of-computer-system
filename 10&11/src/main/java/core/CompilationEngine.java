package core;

import entity.Token;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.XMLWriter;
import util.FileUtil;
import java.io.FileWriter;
import java.io.IOException;

public class CompilationEngine {
    /**
     * @Auther: ncjdjyh
     * @Date: 2019/1/5 22:19
     * @Description: 自顶向下递归语法分析器
     */
    public static final String TARGET_FIX = ".xml";
    private JackTokenizer tokenizer;
    // 抽象语法树根节点 所有的 token 写到这个文档里
    private Document astDocument;
    // 抽象语法树根节点
    private Element root;

    public CompilationEngine(JackTokenizer tokenizer) {
        setup(tokenizer);
        generateAstXML();
    }

    private void generateAstXML() {
        while (tokenizer.hasMoreTokens()) {
            tokenizer.advance();
            Token currentToken = tokenizer.getCurrentToken();
            createNode(currentToken);
        }
    }

    private void setup(JackTokenizer tokenizer) {
        this.tokenizer = tokenizer;
        astDocument = DocumentHelper.createDocument();
        root = astDocument.addElement("tokens");
    }

    private void createNode(Token token) {
        var elementName = token.getTokenType().getAlias();
        var elementText = token.getContent();
        elementText = exchangeXMLBuiltCharacter(elementText);
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
}
