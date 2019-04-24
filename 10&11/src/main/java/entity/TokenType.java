package entity;

/* 字符类型 */
public enum TokenType {
    KEYWORD("keyword"), SYMBOL("symbol"),
    IDENTIFIER("identifier"), INT_CONST("integerConst"),
    STRING_CONST("stringConst");

    String name;
    TokenType(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}