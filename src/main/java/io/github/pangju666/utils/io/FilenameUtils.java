package io.github.pangju666.utils.io;

import javax.activation.MimetypesFileTypeMap;
import java.io.File;
import java.util.regex.Matcher;

/**
 * 文件名工具类
 *
 * @author 胖橘
 * @version 1.0
 * @since 1.0
 *
 * @see org.apache.commons.io.FilenameUtils
 */
public class FilenameUtils extends org.apache.commons.io.FilenameUtils {
    /** 文件路径分隔符 */
    private static final String FILE_PATH_SEPARATOR = String.valueOf(File.separatorChar);
    /** 文件类型分隔符 */
    private static final String TYPE_SPLIT = ".";
    private static final MimetypesFileTypeMap FILE_TYPE_MAP = new MimetypesFileTypeMap();

    /**
     * 将路径分隔符\\替换为/,
     * 如: c:\\file\\userFiles\\temp\\test.doc会被替换为
     * c:/file/userFiles/temp/test.doc
     *
     * @param filePath 文件路径
     * @return 替换后的文件路径
     */
    public static String replacePathSplit(String filePath) {
        if (!filePath.contains("\\") || !filePath.contains("/")) {
            return filePath;
        }
        return filePath.replaceAll(Matcher.quoteReplacement("\\"), FILE_PATH_SEPARATOR);
    }

    /**
     * 修改文件名称
     *
     * @param fileName 文件名
     * @param newFileName 新文件名称，不包含文件类型
     * @return 修改后的文件名称
     */
    public static String changeBaseName(String fileName, String newFileName) {
        String extension = getExtension(fileName);
        return newFileName + TYPE_SPLIT + extension;
    }

    /**
     * 修改文件类型
     *
     * @param fileName 文件名称
     * @param newExtension 新文件后缀
     * @return 修改后的文件名称, 修改失败则返回空字符串
     */
    public static String changeExtension(String fileName, String newExtension) {
        if (!fileName.contains(TYPE_SPLIT)) {
            return fileName;
        }
        return getBaseName(fileName) + TYPE_SPLIT + newExtension;
    }

    /**
     * 获取文件MINE类型, 此方法根据文件后缀进行识别，支持类型较少 <br />
     * 如果需要获取精确的MineType，建议使用{@link FileUtils#getMineType(File)}进行获取
     *
     * @param fileName 文件名
     * @return 文件的MINE类型
     */
    public static String getMineType(String fileName) {
        return FILE_TYPE_MAP.getContentType(fileName);
    }
}
