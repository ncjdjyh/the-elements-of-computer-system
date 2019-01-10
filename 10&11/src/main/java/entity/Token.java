package entity;


import core.JackTokenizer;

public class Token {
    /**
     * @Auther: ncjdjyh
     * @Date: 2019/1/6 17:36
     * @Description:
     */
    private TokenType tokenType;
    private String content;
    private Object value;

    public Token(String content) {
        this.content = content;
        var pair = JackTokenizer.tokenType(content);
        this.tokenType = pair.getValue0();
        this.value = pair.getValue1();
        this.content = content;
    }

    public TokenType getTokenType() {
        return tokenType;
    }

    public String getContent() {
        return content;
    }

    public Object getValue() {
        return value;
    }
}
