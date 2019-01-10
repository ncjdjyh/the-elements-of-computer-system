package core;

import java.io.File;
import java.util.Arrays;

public class JackAnalyzer {
    /**
     * @Auther: ncjdjyh
     * @Date: 2019/1/5 22:19
     * @Description: 建立和调用其他模块
     */
    public static final String SOURCE_FIX = ".jack";
    private static final String RESOURCES_LOCATION = "C:\\Users\\ncjdj\\Desktop\\10";

    public static void main(String[] args) {
        handleResources(RESOURCES_LOCATION);
    }

    public static void handleResources(String directoryName) {
        File file = new File(directoryName.trim());
        if (file.exists()) {
            if (file.isFile()) {
                if (!isTargetFile(file)) {
                    System.out.println("不是 .jack 文件");
                    return;
                }
                transformation(file);
            } else if (file.isDirectory()) {
                // 只过滤出以.jack结尾的文件
                Arrays.asList(file.listFiles()).stream()
                        .filter(JackAnalyzer::isTargetFile)
                        .forEach(JackAnalyzer::transformation);
            }
        }
    }

    public static void transformation(File file) {
        JackTokenizer tokenizer = new JackTokenizer(file);
        CompilationEngine engine = new CompilationEngine(tokenizer);
        engine.printAstFile();
    }

    private static boolean isTargetFile(File file) {
        return file.getName().lastIndexOf(SOURCE_FIX) > 0;
    }
}
