package entity;


import core.JackTokenizer;
import lombok.Data;

import java.security.Key;

@Data
public class Token {
    /**
     * @Author: ncjdjyh
     * @Date: 2019/1/6 17:36
     * @Description:
     */
    private TokenType tokenType;
    // 字符原始内容
    private String content;
    // 普通 token 存放解析出来的值, 关键字 token 存放 Keyword
    private Object value;

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
    }

    public String getValue() {
        if (value instanceof Keyword) {
            return ((Keyword) value).name;
        } else {
            return (String) value;
        }
    }

    public Keyword getKeyword() {
        // value 是关键字时才可以调用
        if (value instanceof Keyword) {
            return (Keyword) value;
        }
        throw new RuntimeException("this is not a keyword token");
    }
}
