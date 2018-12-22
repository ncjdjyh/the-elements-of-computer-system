package _06_1;

import java.io.*;
import java.util.LinkedList;
import java.util.List;

public class Trigger {
    /**
     * @Auther: ncjdjyh
     * @Date: 2018/11/17 12:34
     * @Description: 引导 ASM 文件的翻译
     */
    private static Parser parser = new Parser("src/_06_1/Prog.asm");

    public static void main(String[] args) {
        launch();
    }

    private static void launch() {
        Command currentCommand = parser.getCurrentCommand();
        // 存放翻译出来的二进制代码
        List<String> binaryCodes = new LinkedList<>();
        String code = extractBinaryCode(currentCommand);
        binaryCodes.add(code);
        while (parser.hasMoreCommands()) {
            currentCommand = parser.advance();
            code = extractBinaryCode(currentCommand);
            binaryCodes.add(code);
        }
        printToFile("src/_06_1/Prog.hack", binaryCodes);
    }

    private static String extractBinaryCode(Command currentCommand) {
        String content = currentCommand.getContent();
        String symbol = currentCommand.symbol();
        if (currentCommand.isACommand()) {
            return getACommandBinaryCode(symbol);
        } else if (currentCommand.isCCommand()) {
            return getCCommandBinaryCode(content);
        } else {
            // L 指令的处理
            return null;
        }
    }

   /* 获取 C 指令的二进制代码 */
    public static String getCCommandBinaryCode(String content) {
        StringBuffer sb = new StringBuffer();
        String comp = Code.comp(parser.comp(content));
        String dest = Code.dest(parser.dest(content));
        String jmp = Code.jump(parser.jump(content));
        sb.append(Code.A_COMMAND_PREFIX);
        sb.append(comp);
        sb.append(dest);
        sb.append(jmp);
        return sb.toString();
    }

    /* 获取 A 指令的二进制代码 */
    public static String getACommandBinaryCode(String decStr) {
        StringBuffer code = new StringBuffer("0000000000000000");
        int end = code.length();
        int decInteger = Integer.parseInt(decStr);
        String binStr = Integer.toBinaryString(decInteger);
        int start = end - binStr.length();
        return code.replace(start, end, binStr).toString();
    }

    private static void printToFile(String name, List<String> list) {
        try {
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
