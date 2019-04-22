package entity;

/* 程序结构类型 */
public enum BuiltInType {
    NORMAL("normal"), CLASS("class"), CLASS_VAR_DEC("classVarDec"),
    SUBROUTINE("subroutine"), PARAMETER_LIST("parameterList"), VAR_DEC("varDec"),
    STATEMENTS("statements"), DO("do"), LET("let"), WHILE("while"), RETURN("return"),
    IF("if"), EXPRESSION("expression"), TERM("term"), EXPRESSION_LIST("expressionList"),;

    String name;

    BuiltInType(String name) {
        this.name = name;
    }
}
