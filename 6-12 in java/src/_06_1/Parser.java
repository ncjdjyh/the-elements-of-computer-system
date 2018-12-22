package _06_1;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.lang3.StringUtils;
import org.junit.Assert;

/*  TODO 代码优化! */
public class Parser {
    /**
     * @Auther: ncjdjyh
     * @Date: 2018/11/14 17:59
     * @Description: ASM 词法分析
     */
    private String fileName;
    // 保存所有指令
    private List<Command> instructions;
    // 当前指令
    private Command currentCommand;
    private int currentCommandIndex = 0;
    private int lengthOfInstruct = 0;

    public Parser(String fileName) {
        this.fileName = fileName;
        instructions = new ArrayList<>();
        loadASMFile(fileName);
    }

    public Command getCurrentCommand() {
        return currentCommand;
    }

    public List<Command> getInstructions() {
        return instructions;
    }

    private CommandType commandType(String str) {
        // A 指令 @xxx
        if (str.matches("^@[a-zA-Z0-9]+")) {
            return CommandType.A_COMMAND;
        } else if (str.matches("^\\([A-Za-z]*\\)$")) {
            return CommandType.L_COMMAND;
        } else {
            return CommandType.C_COMMAND;
        }
    }

    private void loadASMFile(String name) {
        try {
            FileReader fis = new FileReader(new File(name));
            BufferedReader br = new BufferedReader(fis);
            String str = null;
            while((str = br.readLine()) != null) {
                // 去除注释和空行
                if (str.isEmpty() || str.startsWith("//")) {
                    continue;
                }
                String trimStr = clearUselessContent(str);
                Command command = new Command(trimStr, commandType(trimStr));
                instructions.add(command);
            }
            fis.close();
            br.close();
            currentCommand = getCurrentFromInstructions(0);
            lengthOfInstruct = instructions.size();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /* 去除行内注释 和 () */
    private String clearUselessContent(String str) {
        if (str.contains("//")) {
            String trimStr = str.split("//")[0].trim();
            return trimStr;
        }
        return str;
    }

    public boolean hasMoreCommands() {
        return currentCommandIndex < lengthOfInstruct - 1;
    }

    /* 下一条指令 */
    public Command advance() {
        currentCommandIndex++;
        currentCommand = getCurrentFromInstructions(currentCommandIndex);
        return currentCommand;
    }

    private Command getCurrentFromInstructions(int currentInstructIndex) {
        return instructions.get(currentInstructIndex);
    }

    /* 设置为可变类型的参数便于测试 */
    /* 返回 dest 助记符 */
    public String dest(String... command) {
        boolean emptyArgs = command[0].isEmpty();
        String content = emptyArgs ? currentCommand.getContent() : command[0];
        boolean have = content.contains("=");
        if (have) {
            return StringUtils.substringBefore(content, "=");
        } else {
            return "";
        }
    }

    /* 返回 jump 助记符 */
    public String jump(String... command) {
        boolean emptyArgs = command[0].isEmpty();
        String content = emptyArgs ? currentCommand.getContent() : command[0];
        boolean have = content.contains(";");
        if (have) {
            return StringUtils.substringAfter(content, "=");
        } else {
            return "";
        }
    }

    /* 返回 comp 助记符 */
    public String comp(String... command) {
        boolean emptyArgs = command[0].isEmpty();
        String content = emptyArgs ? currentCommand.getContent() : command[0];
        String preCMD = content.substring(dest(content).length(), content.length() - (jump(content).length()));
        String compCMD = preCMD.replaceAll("[;=]", "");
        boolean must = !StringUtils.isEmpty(compCMD);
        Assert.assertTrue("command 中一定有 comp 部分", must);
        return compCMD;
    }
}
