package _06_2;

import org.junit.Assert;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.LinkedList;
import java.util.List;

public class Trigger {
    /**
     * @Auther: ncjdjyh
     * @Date: 2018/11/17 12:34
     * @Description: 引导 ASM 文件的翻译
     */
    private Parser parser = new Parser("src/_06_2/Prog.asm");
    private SymbolTable symbolTable = SymbolTable.getInstance();
    // 存放翻译出来的二进制代码
    private List<String> binaryCodes = new LinkedList<>();

    public void __start() {
        // 第一次读取指令, 只建立 table
        setupSymbolTable();
        launch();
    }

    private void launch() {
        // 第二次读取指令, 去除 L 指令
        parser.filterLCommand();
        parser.reset();

        Command currentCommand = parser.getCurrentCommand();
        extractCommandBinaryCode(currentCommand);
        while (parser.hasMoreCommands()) {
            currentCommand = parser.advance();
            extractCommandBinaryCode(currentCommand);
        }
        printToFile("src/_06_2/Prog.hack");
    }

    private void setupSymbolTable() {
        Command currentCommand = parser.getCurrentCommand();
        根据命令类型建立符号表(currentCommand);
        while (parser.hasMoreCommands()) {
            currentCommand = parser.advance();
            根据命令类型建立符号表(currentCommand);
        }
    }

    private void 根据命令类型建立符号表(Command currentCommand) {
        String symbol = currentCommand.symbol();
        int nextROM = symbolTable.getNextAvailableROMAddress();

        if (currentCommand.isNormalCommand()) {
            // A 或 C
            symbolTable.increaseNextAvailableROMAddress();
        } else {
            // 是一个 L 指令 (xxx)
            // 将符号与下一个可用的 rom 映射, 然后通过查表就可以找到它
            symbolTable.addEntry(symbol, nextROM);
        }
    }

    private void extractCommandBinaryCode(Command currentCommand) {
        String content = currentCommand.getContent();
        Assert.assertTrue("现在一定是 A 或者 C 指令", currentCommand.isNormalCommand());
        if (currentCommand.isACommand()) {
            String symbol = currentCommand.symbol();
            extractACommandBinaryCode(symbol);
        } else if (currentCommand.isCCommand()) {
            extractCCommandBinaryCode(content);
        }
    }

   /* 获取 C 指令的二进制代码 */
    public void extractCCommandBinaryCode(String content) {
        StringBuffer sb = new StringBuffer();
        String comp = Code.comp(parser.comp(content));
        String dest = Code.dest(parser.dest(content));
        String jmp = Code.jump(parser.jump(content));
        sb.append(Code.A_COMMAND_PREFIX);
        sb.append(comp);
        sb.append(dest);
        sb.append(jmp);
        binaryCodes.add(sb.toString());
    }

    /* 获取 A 指令的二进制代码 */
    public void extractACommandBinaryCode(String symbol) {
        StringBuffer code = new StringBuffer("0000000000000000");
        boolean isNumber = symbol.matches("^[0-9]\\d*$");
        int endIndex = code.length();
        Integer address = -1;

        if (isNumber) {
            address = Integer.parseInt(symbol);
        } else {
            if (symbolTable.contains(symbol)) {
                address = symbolTable.getAddress(symbol);
            } else {
                // 没找到地址 说明 symbol 是一个变量
                address = symbolTable.getNextAvailableRAMAaddress();
                symbolTable.increaseNextAvailableRAMAddress();
                symbolTable.addEntry(symbol, address);
            }
        }

        String binStr = Integer.toBinaryString(address);
        int start = endIndex - binStr.length();
        String reloadCode =  code.replace(start, endIndex, binStr).toString();
        binaryCodes.add(reloadCode);
    }


    private void printToFile(String name) {
        try {
            List<String> list = binaryCodes;
            FileWriter fw = new FileWriter(new File(name));
            PrintWriter bw = new PrintWriter(fw);
            list.forEach(l -> bw.println(l));
            fw.close();
            bw.close();
            System.out.println("done");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
