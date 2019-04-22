package core;


import cn.hutool.core.io.file.FileReader;

import java.io.File;
import java.util.Arrays;

public class JackAnalyzer {
    /**
     * @Author: ncjdjyh
     * @Date: 2019/1/5 22:19
     * @Description: 建立和调用其他模块
     */
    public static final String SOURCE_FIX = ".jack";
    private static final String RESOURCES_LOCATION = "test.jack";

    public static void main(String[] args) {
        handleResources(RESOURCES_LOCATION);
    }

    public static void handleResources(String directoryName) {
        FileReader fileReader = new FileReader(directoryName.trim());
        var file = fileReader.getFile();
        // 只处理以 .jack 结尾的文件
        if (file.exists()) {
            if (file.isFile()) {
                if (!isTargetFile(file)) {
                    System.out.println("不是 .jack 文件");
                    return;
                }
                transformation(file);
            } else if (file.isDirectory()) {
                Arrays.asList(file.listFiles()).stream()
                        .filter(JackAnalyzer::isTargetFile)
                        .forEach(JackAnalyzer::transformation);
            }
        }
    }

    public static void transformation(File file) {
        JackTokenizer tokenizer = JackTokenizer.getInstance(file);
        CompilationEngine engine = CompilationEngine.getInstance(tokenizer);
        engine.generateAstXML();
        engine.printAstFile();
        System.out.println("done");
    }

    private static boolean isTargetFile(File file) {
        return file.getName().lastIndexOf(SOURCE_FIX) > 0;
    }
}
