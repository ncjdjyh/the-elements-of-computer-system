package _06_2;

import java.util.HashMap;

public class Code {
    /**
     * @Auther: ncjdjyh
     * @Date: 2018/11/17 12:33
     * @Description: 获取 ASM 指令二进制 code
     */
    public static final String A_COMMAND_PREFIX = "111";
    // dest 助记符转二进制码翻译表
    public static HashMap<String, String> destTable;
    // comp 助记符转二进制码翻译表
    public static HashMap<String, String> compTable;
    // jump 助记符转二进制码翻译表
    public static HashMap<String, String> jumpTable;

    /**
     * 初始化码表
     */
    static {
        destTable = new HashMap<>();
        compTable = new HashMap<>();
        jumpTable = new HashMap<>();

        destTable.put("", "000");
        destTable.put("M", "001");
        destTable.put("D", "010");
        destTable.put("MD", "011");
        destTable.put("A", "100");
        destTable.put("AM", "101");
        destTable.put("AD", "110");
        destTable.put("AMD", "111");

        compTable.put("0", "0101010");
        compTable.put("1", "0111111");
        compTable.put("-1", "0111010");
        compTable.put("D", "0001100");
        compTable.put("A", "0110000");
        compTable.put("!D", "0001101");
        compTable.put("!A", "0110001");
        compTable.put("-D", "0001111");
        compTable.put("-A", "0110011");
        compTable.put("D+1", "0011111");
        compTable.put("A+1", "0110111");
        compTable.put("D-1", "0001110");
        compTable.put("A-1", "0110010");
        compTable.put("D+A", "0000010");
        compTable.put("D-A", "0010011");
        compTable.put("A-D", "0000111");
        compTable.put("D&A", "0000000");
        compTable.put("D|A", "0010101");
        compTable.put("M", "1110000");
        compTable.put("!M", "1110001");
        compTable.put("-M", "1110011");
        compTable.put("M+1", "1110111");
        compTable.put("M-1", "1110010");
        compTable.put("D+M", "1000010");
        compTable.put("D-M", "1010011");
        compTable.put("M-D", "1000111");
        compTable.put("D&M", "1000000");
        compTable.put("D|M", "1010101");

        jumpTable.put("", "000");
        jumpTable.put("JGT", "001");
        jumpTable.put("JEQ", "010");
        jumpTable.put("JGE", "011");
        jumpTable.put("JLT", "100");
        jumpTable.put("JNE", "101");
        jumpTable.put("JLE", "110");
        jumpTable.put("JMP", "111");
    }

    /**
     * 获取 dest 的二进制码字符串
     * @param exp 助记符
     * @return
     */
    public static String dest(final String exp) {
        String ret = destTable.get(exp);
        if (ret == null) {
            throw new RuntimeException("Can not find dest expresstion '" + exp + "'");
        }
        return ret;
    }

    /**
     * 获取 comp 的二进制码字符串
     * @param exp 助记符
     * @return
     */
    public static String comp(final String exp) {
        String ret = compTable.get(exp);
        if (ret == null) {
            throw new RuntimeException("Can not find comp expresstion '" + exp + "'");
        }
        return ret;
    }

    /**
     * 获取 jump 的二进制码字符串
     * @param exp 助记符
     * @return
     */
    public static String jump(final String exp) {
        String ret = jumpTable.get(exp);
        if (ret == null) {
            throw new RuntimeException("Can not find jump expresstion '" + exp + "'");
        }
        return ret;
    }
}
