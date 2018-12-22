package _06_2;

/* A 指令 @xxx, xxx 是符号或者十进制
*  C 指令 dest=comp;jump
*  L 指令(伪指令) (xxx) 中 xxx 是符号 (给 goto 语句做标识)
*  */
public enum CommandType {
    A_COMMAND, C_COMMAND, L_COMMAND,
}
