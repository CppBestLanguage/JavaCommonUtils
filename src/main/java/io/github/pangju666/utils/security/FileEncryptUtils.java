package io.github.pangju666.utils.security;

import io.github.pangju666.utils.io.FileUtils;
import org.apache.commons.io.IOUtils;

import javax.validation.constraints.NotNull;
import java.io.*;

/**
 * 文件字节加密工具类
 *
 * @author pangju666
 * @version 1.0 2020-4-4
 * @since 1.2
 */
public class FileEncryptUtils {
    private final static int DEFAULT_KEY = 91291358;
    private final static int FILE_EOF = -1;

    /**
     * 文件加密
     *
     * @param srcFile 源文件
     * @throws IOException 加密失败或文件不存在时抛出
     * @since 1.0
     */
    public void encodeFile(@NotNull File srcFile) throws IOException {
        encodeFile(srcFile, DEFAULT_KEY);
    }

    /**
     * 文件加密
     *
     * @param srcFile 源文件
     * @param key 加密所用key
     * @throws IOException 加密失败或文件不存在时抛出
     * @since 1.0
     */
    public void encodeFile(@NotNull File srcFile, int key) throws IOException {
       File destFile = FileUtils.getFile(FileUtils.getTempDirectory(), srcFile.getName());
        encodeFile(srcFile, destFile, key);
       FileUtils.copyFile(destFile, srcFile);
       FileUtils.forceDelete(destFile);
    }

    /**
     * 文件加密
     *
     * @param srcFile 源文件
     * @param destFile 目标文件
     * @throws IOException 加密失败或文件不存在时抛出
     * @since 1.0
     */
    public static void encodeFile(@NotNull File srcFile, @NotNull File destFile) throws IOException {
        encodeFile(srcFile, destFile, DEFAULT_KEY);
    }

    /**
     * 文件加密
     *
     * @param srcFile 源文件
     * @param destFile 目标文件
     * @param key 加密所用key
     * @throws IOException 加密失败或文件不存在时抛出
     * @since 1.0
     */
    public static void encodeFile(@NotNull File srcFile, @NotNull File destFile, int key) throws IOException {
        try(BufferedInputStream bis = IOUtils.buffer(FileUtils.openInputStream(srcFile));
            BufferedOutputStream bos = IOUtils.buffer(FileUtils.openOutputStream(destFile))) {
            int ch = bis.read();
            while (ch != FILE_EOF) {
                bos.write(ch ^ key);
                ch = bis.read();
            }
        }
    }
}
