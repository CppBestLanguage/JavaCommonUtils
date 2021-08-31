package io.github.pangju666.utils.io;

import net.sf.jmimemagic.*;

import java.io.*;

/**
 * 文件处理工具，主要包含写入和读取文件数据
 *
 * @author 胖橘
 * @version 1.0
 * @since 1.0
 *
 * @see org.apache.commons.io.FileUtils
 */
public class FileUtils extends org.apache.commons.io.FileUtils {
    protected FileUtils() {}

    /**
     * 获取文件MINE类型
     *
     * @param file 待解析文件
     * @return 文件MINE类型
     */
    public static String getMineType(File file) {
        try {
            MagicMatch match = Magic.getMagicMatch(file, true);
            return match.getMimeType();
        } catch (MagicParseException | MagicMatchNotFoundException | MagicException e) {
            throw new RuntimeException("文件Mine类型获取失败", e);
        }
    }
}