package entity;


import core.JackTokenizer;
import lombok.Data;

@Data
public class  Token {
    /**
     * @Auther: ncjdjyh
     * @Date: 2019/1/6 17:36
     * @Description:
     */
    private TokenType tokenType;
    private BuiltInType builtInType;
    private String content;
    private String value;

    public Token(String content) {
        this.content = content;
        var pair = JackTokenizer.tokenType(content);
        this.builtInType = JackTokenizer.getBuiltInType(content);
        this.tokenType = pair.getValue0();
        this.value = pair.getValue1();
        this.content = content;
    }
}
