package entity;


import core.JackTokenizer;
import lombok.Data;

@Data
public class Token {
    /**
     * @Author: ncjdjyh
     * @Date: 2019/1/6 17:36
     * @Description:
     */
    private TokenType tokenType;
    private String content;
    // 普通 token 存放解析出来的值, 关键字 token 存放 Keyword
    private Object value;
    private BuiltInType builtInType;

    public Token(String content) {
        this.content = content;
        var pair = JackTokenizer.tokenType(content);
        this.tokenType = pair.getValue0();
        if (this.tokenType == TokenType.KEYWORD) {
            this.value = JackTokenizer.keyword(content);
        } else {
            this.value = pair.getValue1();
        }
        this.content = content;
//        this.builtInType = JackTokenizer.getBuiltInType(this.value, content);
    }
}
