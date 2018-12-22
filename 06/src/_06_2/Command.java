package _06_2;

public class Command {
    /**
     * @Auther: ncjdjyh
     * @Date: 2018/11/22 14:25
     * @Description: 指令类
     */
    private String content;
    private CommandType type;

    public Command(String content, CommandType type) {
        this.content = content;
        this.type = type;
    }

    public String getContent() {
        return content;
    }

    public boolean isSymbolCommand() {
        return isACommand() || isLCommand();
    }

    public Boolean isACommand() {
        return type == CommandType.A_COMMAND;
    }

    public Boolean isNormalCommand() {
        return isACommand() || isCCommand();
    }

    public Boolean isCCommand() {
        return type == CommandType.C_COMMAND;
    }

    public Boolean isLCommand() {
        return type == CommandType.L_COMMAND;
    }

    /* A L 指令才能调用 代表 @ 或者 ()*/
    public String symbol() {
        String ret = isACommand()
                ? content.split("@")[1]
                : content.substring(1, content.length() - 1);
        return ret;
    }
}
