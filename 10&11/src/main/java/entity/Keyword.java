package entity;

/**
 * @Author: ncjdjyh
 * @FirstInitial: 2019/4/22
 * @Description: 关键字
 */
public enum Keyword {
    CLASS("class"), METHOD("method"), INT("int"), FUNCTION("function"), BOOLEAN("boolean"),
    CONSTRUCTOR("constructor"), CHAR("char"), VOID("void"), VAR("var"),
    STATIC("static"), FIELD("field"), LET("let"), DO("do"), IF("if"),RETURN("return"),
    ELSE("else"), WHILE("while"), TRUE("true"), FALSE("false"), NULL("null"), THIS("this");

    String name;

    public String getName() {
        return name;
    }

    Keyword(String name) {
        this.name = name;
    }
}
