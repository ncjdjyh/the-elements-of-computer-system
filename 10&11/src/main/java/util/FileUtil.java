package util;

import java.io.File;

public class FileUtil {
    /**
     * @Auther: ncjdjyh
     * @Date: 2019/1/7 14:56
     * @Description:
     */

    /* 替换文件后缀后返回新文件 */
    public static File fetchExchangeSuffixFile(File file, String newSuffix) {
        String filename = file.getAbsolutePath();
        if (filename.indexOf(".") >= 0) {
            if (!newSuffix.contains(".")) {
                newSuffix = "." + newSuffix;
            }
            filename = filename.substring(0, filename.lastIndexOf("."));
        }
        return new File(filename + newSuffix);
    }
}
