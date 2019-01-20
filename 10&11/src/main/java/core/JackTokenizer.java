package core;

import entity.BuiltInType;
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
     * @Auther: ncjdjyh
     * @Date: 2019/1/5 22:19
     * @Description: 子元转换器
     */
    private File file;
    private List<Token> tokenList;
    private int currentTokenIndex;
    private Token currentToken;
    public static JackTokenizer singleton;

    private static final int MIN_INT = 0;
    private static final int MAX_INT = 0x7FFF;
    private static final String IDENTIFIER = "^[a-zA-Z_]{1}[a-zA-Z0-9_]*";
    private static final String STRING_CONST = "^\"[^\"\n]*\"$";
    private static final String SINGLELINE_COMMENT = "//";
    private static final String MULT_START_COMMENT = "/*";
    private static final String MULT_API_START_COMMENT = "/**";
    private static final String MULT_END_COMMENT = "*/";

    public static HashSet<String> keywordSet;
    public static HashSet<String> symbolSet;

    public static final String[] KEYWORDS =
            {"class", "constructor",
                    "function", "method", "field", "static", "var", "int", "char",
                    "boolean", "void", "true", "false", "null", "this", "let", "do",
                    "if", "else", "while", "return"};
    public static final String[] SYMBOLS =
            {"{", "}", "(", ")", "[", "]", ".", ",", ";",
                    "+", "-", "*", "/", "&", "|", "<", ">", "=", "~"};

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
        extractTokenList(file);
    }

    private void extractTokenList(File file) {
        try {
            this.file = file;
            // TODO 1. 为了方便直接使用了 api 然后对结果再进行处理, 其实不是一个好的方法因为读取了两次 token 2. 不方便进行差错检查
            var lines = FileUtils.readLines(file);
            lines = refineCommandLines(lines);
            var tokenStr = join(lines, StringUtils.EMPTY).split("\\s+");
            Arrays.asList(tokenStr)
                    .stream()
                    .map(Token::new)
                    .forEach(e -> tokenList.add(e));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void setup() {
        tokenList = new LinkedList<>();
        this.currentTokenIndex = 0;
        this.currentToken = null;
    }

    public Token getCurrentToken() {
        return currentToken;
    }

    public File getFile() {
        return file;
    }

    public boolean hasMoreTokens() {
        return tokenList.size() > currentTokenIndex;
    }

    public void advance() {
        if (hasMoreTokens()) {
            currentToken = tokenList.get(currentTokenIndex);
            currentTokenIndex++;
        }
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
                .map(JackTokenizer::change)
                .map(JackTokenizer::handleInnerLineComment)
                .map(JackTokenizer::reloadCommandAccordingToSymbol)
                .collect(Collectors.toList());
        return commandLines;
    }

    /* TODO 用很 tricky 的方式解决形如 ""content"" 的字符串问题 感觉还是要自己一行一行的来读, 便于控制~ */
    private static String change(String command) {
        Pattern p = Pattern.compile("\"(.*)\"");
        Matcher m = p.matcher(command);
        while (m.find()) {
            command = m.group(1);
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

    /* 如果遇到 symbol 要进行分隔 目前使用的分隔符是空格 ~ */
    private static String reloadCommandAccordingToSymbol(String command) {
        for (var s : symbolSet) {
            if (command.contains(s)) {
                command = replace(command, s, SPACE + s + SPACE);
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
            throw new RuntimeException("Unknow token type!" + "\n");
        }
    }

    public static BuiltInType getBuiltInType(String content) {
        if (content.matches("field|static")) {
            return BuiltInType.CLASS_VAR_DEC;
        } else {
            return BuiltInType.NORMAL;
        }
    }
}
