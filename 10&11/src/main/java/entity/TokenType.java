package entity;

public enum TokenType {
    KEYWORD("keyword"), SYMBOL("symbol"),
    IDENTIFIER("identifier"), INT_CONST("integerConst"),
    STRING_CONST("stringConst");

    String alias;
    TokenType(String alias) {
        this.alias = alias;
    }

    public String getAlias() {
        return alias;
    }
}