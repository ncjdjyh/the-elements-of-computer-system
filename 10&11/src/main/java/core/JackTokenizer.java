package core;

import entity.Keyword;
import entity.Token;
import entity.TokenType;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.javatuples.Pair;

import java.io.*;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static org.apache.commons.lang3.StringUtils.*;
import static org.apache.commons.lang3.StringUtils.SPACE;

public class JackTokenizer {
    /**
     * @Author: ncjdjyh
     * @Date: 2019/1/5 22:19
     * @Description: 子元转换器
     */
    private File file;
    private List<Token> tokenList;
    private int currentTokenIndex;
    private Token currentToken;
    public static JackTokenizer singleton;

    public static final int MIN_INT = 0;
    public static final int MAX_INT = 0x7FFF;
    public static final String IDENTIFIER = "^[a-zA-Z_]{1}[a-zA-Z0-9_]*";
    public static final String STRING_CONST = "^\"[^\"]*\"$";
    public static final String SINGLELINE_COMMENT = "//";
    public static final String MULT_START_COMMENT = "/*";
    public static final String MULT_API_START_COMMENT = "/**";
    public static final String MULT_END_COMMENT = "*/";
    public static final String STRING_SEPARATOR = "\\";

    public static HashSet<String> keywordSet;
    public static HashSet<String> symbolSet;

    public static final String[] KEYWORDS =
            {"class", "constructor", "function", "method", "field", "static", "var", "int", "char", "boolean", "void",
                    "true", "false", "null", "this", "let", "do", "if", "else", "while", "return"};
    public static final String[] SYMBOLS =
            {"{", "}", "(", ")", "[", "]", ".", ",", ";", "+", "-", "*", "/", "&", "|", "<", ">", "=", "~"};

    static {
        keywordSet = new HashSet<>();
        keywordSet.addAll(Arrays.asList(KEYWORDS));
        symbolSet = new HashSet<>();
        symbolSet.addAll(Arrays.asList(SYMBOLS));
    }

    public static JackTokenizer getInstance(File file) {
        if (singleton == null) {
            singleton = new JackTokenizer(file);
        }
        return singleton;
    }

    public static JackTokenizer getInstance() {
        return Objects.requireNonNull(singleton);
    }

    private JackTokenizer(File file) {
        setup();
        // 把 token 解析出来放到一个 list 中
        extractTokenList(file);
    }

    public static Keyword keyword(String content) {
        if (content.equalsIgnoreCase("class")) {
            return Keyword.CLASS;
        } else if (content.equalsIgnoreCase("method")) {
            return Keyword.METHOD;
        } else if (content.equalsIgnoreCase("int")) {
            return Keyword.INT;
        } else if (content.equalsIgnoreCase("function")) {
            return Keyword.FUNCTION;
        } else if (content.equalsIgnoreCase("boolean")) {
            return Keyword.BOOLEAN;
        } else if (content.equalsIgnoreCase("constructor")) {
            return Keyword.CONSTRUCTOR;
        } else if (content.equalsIgnoreCase("char")) {
            return Keyword.CHAR;
        } else if (content.equalsIgnoreCase("void")) {
            return Keyword.VOID;
        } else if (content.equalsIgnoreCase("var")) {
            return Keyword.VAR;
        } else if (content.equalsIgnoreCase("static")) {
            return Keyword.STATIC;
        } else if (content.equalsIgnoreCase("field")) {
            return Keyword.FIELD;
        } else if (content.equalsIgnoreCase("let")) {
            return Keyword.LET;
        } else if (content.equalsIgnoreCase("do")) {
            return Keyword.DO;
        } else if (content.equalsIgnoreCase("if")) {
            return Keyword.IF;
        } else if (content.equalsIgnoreCase("else")) {
            return Keyword.ELSE;
        } else if (content.equalsIgnoreCase("while")) {
            return Keyword.WHILE;
        } else if (content.equalsIgnoreCase("return")) {
            return Keyword.RETURN;
        } else if (content.equalsIgnoreCase("false")) {
            return Keyword.FALSE;
        } else if (content.equalsIgnoreCase("true")) {
            return Keyword.TRUE;
        } else if (content.equalsIgnoreCase("null")) {
            return Keyword.NULL;
        } else if (content.equalsIgnoreCase("this")) {
            return Keyword.THIS;
        } else {
            throw new RuntimeException("错误的关键字");
        }
    }

    private void extractTokenList(File file) {
        try {
            this.file = file;
            var lines = FileUtils.readLines(file);
            lines = refineCommandLines(lines);
            var tokenStr = StringUtils.join(lines, StringUtils.EMPTY);
            var tokens = tokenStr.split("\\s+");
            Arrays.asList(tokens)
                    .stream()
                    .map(e -> e.replace(STRING_SEPARATOR, SPACE))
                    .map(String::trim)
                    .map(Token::new)
                    .forEach(e -> tokenList.add(e));
            // 当前 token 初始化为第一个
            currentToken = tokenList.get(0);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void setup() {
        tokenList = new LinkedList<>();
        this.currentTokenIndex = 0;
    }

    public Token getCurrentToken() {
        return currentToken;
    }

    public TokenType getCurrentTokenType() {
        return currentToken.getTokenType();
    }

    public String getCurrentTokenValue() {
        return currentToken.getValue();
    }

    public File getFile() {
        return file;
    }

    public boolean hasMoreTokens() {
        return tokenList.size() - 1 > currentTokenIndex;
    }

    /* 指令向后读取一位 */
    public Token advance() {
        if (hasMoreTokens()) {
            currentTokenIndex++;
            currentToken = tokenList.get(currentTokenIndex);
            return currentToken;
        }
        throw new RuntimeException("no more tokens");
    }

    /* 不改变当前 token 只是向前看一位 */
    public Token peekAdvance() {
        if (hasMoreTokens()) {
            return tokenList.get(currentTokenIndex + 1);
        }
        throw new RuntimeException("no more tokens");
    }

    /* 指令向前读取一位 */
    public Token back() {
        currentTokenIndex--;
        currentToken = tokenList.get(currentTokenIndex);
        return currentToken;
    }

    public List<String> refineCommandLines(List<String> commandLines) {
        removeMultComment(commandLines);
        // 对单行注释和关键字分隔的处理
        commandLines = commandLines
                .stream()
                .map(String::trim)
                .filter(e -> {
                    boolean notEmpty = isNotBlank(e);
                    boolean notComment = !startsWith(e, SINGLELINE_COMMENT);
                    return notComment && notEmpty;
                })
                .map(JackTokenizer::handleString)
                .map(JackTokenizer::handleInnerLineComment)
                .map(JackTokenizer::reloadCommandAccordingToSymbol)
                .collect(Collectors.toList());
        return commandLines;
    }

    /* 用很 tricky 的方式处理了字符串中有空格时出现的问题 */
    private static String handleString(String command) {
        Pattern p = Pattern.compile("\"(.*)\"");
        Matcher m = p.matcher(command);
        if (m.find()) {
            // 字符串类型
            var sm = m.group(1);
            sm = "\"" + sm.replace(SPACE, STRING_SEPARATOR);
            var sh = StringUtils.substringBefore(command, "\"");
            var st = "\"" + StringUtils.substringAfterLast(command, "\"");
            command = sh + sm + st + SPACE;
        }
        return command;
    }

    private void removeMultComment(List<String> commandLines) {
        boolean stillInMultComment = false;
        HashSet uselessCommandSet = new HashSet();
        for (int i = 0; i < commandLines.size(); i++) {
            var command = commandLines.get(i);
            if (startWithMultComment(command)) {
                stillInMultComment = true;
            }
            if (stillInMultComment) {
                uselessCommandSet.add(command);
            }
            if (contains(command, MULT_END_COMMENT)) {
                stillInMultComment = false;
            }
        }
        commandLines.removeAll(uselessCommandSet);
    }

    private static String handleInnerLineComment(String command) {
        // 去除行内注释
        if (command.contains(SINGLELINE_COMMENT)) {
            command = StringUtils.substringBefore(command, SINGLELINE_COMMENT);
        }
        return command;
    }

    private boolean startWithMultComment(String command) {
        return startsWithAny(command, MULT_START_COMMENT, MULT_API_START_COMMENT);
    }

    /* 如果遇到 symbol 要进行分隔 目前使用的分隔符是 ~ */
    private static String reloadCommandAccordingToSymbol(String command) {
        for (var s : symbolSet) {
            if (command.contains(s)) {
                command = StringUtils.replace(command, s, SPACE + s + SPACE);
            }
        }
        return command;
    }

    /* 返回一个二元组 pair(tokenType 和他所对应的值) */
    public static Pair<TokenType, String> tokenType(String content) {
        if (keywordSet.contains(content)) {
            return Pair.with(TokenType.KEYWORD, content);
        } else if (symbolSet.contains(content)) {
            return Pair.with(TokenType.SYMBOL, content);
        } else if (content.matches("\\d+")) {
            int cst = Integer.parseInt(content);
            if (cst < MIN_INT || cst > MAX_INT) {
                throw new RuntimeException("int constant is out of range at line ");
            }
            return Pair.with(TokenType.INT_CONST, content);
        } else if (content.matches(IDENTIFIER)) {
            return Pair.with(TokenType.IDENTIFIER, content);
        } else if (content.matches(STRING_CONST)) {
            return Pair.with(TokenType.STRING_CONST, content);
        } else {
            throw new RuntimeException("Unknow token type!  " + content);
        }
    }
}
