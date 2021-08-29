package io.github.pangju666.utils.io;

import org.apache.commons.codec.digest.Md5Crypt;
import org.apache.commons.io.FilenameUtils;

import java.io.*;
import java.util.UUID;
import java.util.regex.Matcher;

/**
 * 文件处理工具，主要包含写入和读取文件数据
 *
 * @author 胖橘
 * @version 1.0 2020-11-16
 * @since 1.0
 */
public class FileUtils extends org.apache.commons.io.FileUtils {
    /** 文件路径分隔符 */
    private static final String FILE_PATH_SEPARATOR = String.valueOf(File.separatorChar);
    /** 文件类型分隔符 */
    public static final String TYPE_SPLIT = ".";

    /** 构造方法私有化，防止生成实例 */
    protected FileUtils() {}

    /**
     * 获取文件绝对路径
     *
     * @param parentPath 父路径
     * @param filePath 文件路径
     * @return 文件绝对路径
     * @since 1.0
     */
    public static String getAbsolutePath(String parentPath, String filePath) {
        parentPath = replaceSplit(parentPath);
        filePath = replaceSplit(filePath);
        // 判断是否具备相对路径
        return filePath.startsWith(FILE_PATH_SEPARATOR) ? parentPath + filePath : parentPath + FILE_PATH_SEPARATOR + filePath;
    }

    /**
     * 获取文件父路径
     *
     * @param filePath 文件路径
     * @return 父路径
     * @since 1.0
     */
    public static String getParentPath(String filePath) {
        filePath = replaceSplit(filePath);
        int index = filePath.lastIndexOf(FILE_PATH_SEPARATOR);
        return index == -1 ? "" : filePath.substring(0, index);
    }

    /**
     * 将路径分隔符\\替换为/,
     * 如: c:\\file\\userFiles\\temp\\test.doc会被替换为
     * c:/file/userFiles/temp/test.doc
     *
     * @param filePath 文件路径
     * @return 替换后的文件路径
     * @since 1.0
     */
    public static String replaceSplit(String filePath) {
        if (!filePath.contains("\\") || !filePath.contains("/")) {
            return filePath;
        }
        return filePath.replaceAll(Matcher.quoteReplacement("\\"), FILE_PATH_SEPARATOR);
    }

    /**
     * 修改文件名称, 如果存在本地文件则将修改应用至本地文件
     *
     * @param fileName 文件名
     * @param newFileName 新文件名称，不包含文件类型
     * @return 修改后的文件绝对路径
     * @since 1.0
     */
    public static String changeFileName(String fileName, String newFileName) {
        return changeFileName(fileName, newFileName, true);
    }

    /**
     * 修改文件名称
     *
     * @param fileName 文件名
     * @param newFileName 新文件名称，不包含文件类型
     * @param apply 是否修改本地文件
     * @return 修改后的文件绝对路径
     * @since 1.0
     */
    public static String changeFileName(String fileName, String newFileName, boolean apply) {
        String parentPath = getParentPath(fileName);
        return changeFileName(parentPath, fileName, newFileName, apply);
    }

    /**
     * 修改文件名称, 如果存在本地文件则将修改应用至本地文件
     *
     * @param parentPath 父路径，可为空
     * @param fileName 文件名
     * @param newFileName 新文件名称，不包含文件类型
     * @return 修改后的文件名称
     * @since 1.0
     */
    public static String changeFileName(String parentPath, String fileName, String newFileName) {
        return changeFileName(parentPath, fileName, newFileName, true);
    }

    /**
     * 修改文件名称
     *
     * @param parentPath 父路径，可为空
     * @param fileName 文件名
     * @param newFileName 新文件名称，不包含文件类型
     * @param apply 是否修改本地文件
     * @return 修改后的文件名称
     * @since 1.0
     */
    public static String changeFileName(String parentPath, String fileName,
                                        String newFileName, boolean apply) {
        fileName = getAbsolutePath(parentPath, fileName);
        newFileName = getAbsolutePath(parentPath, newFileName);
        File oldFile = new File(fileName);
        if (oldFile.exists() && apply) {
            File newFile = new File(newFileName);
            oldFile.renameTo(newFile);
        }
        return newFileName;
    }

    /**
     * 修改文件类型
     *
     * @param fileName 文件名
     * @param newFileType 新文件类型
     * @return 修改后的文件名称
     * @since 1.0
     */
    public static String changeFileType(String fileName, String newFileType) {
        return changeFileType(fileName, newFileType, false);
    }

    /**
     * 修改文件类型
     *
     * @param fileName 文件名
     * @param newFileType 新文件类型
     * @param apply 是否修改本地文件
     * @return 修改后的文件名称
     * @since 1.0
     */
    public static String changeFileType(String fileName, String newFileType, boolean apply) {
        String parentPath = getParentPath(fileName);
       return changeFileType(parentPath, fileName, newFileType, apply);
    }

    /**
     * 修改文件类型, 如果存在本地文件则将修改应用至本地文件
     *
     * @param parentPath 父路径，不需要此项时可为空
     * @param fileName 文件名称
     * @param newFileType 新文件类型
     * @return 修改后的文件名称, 修改失败则返回空字符串
     * @since 1.0
     */
    public static String changeFileType(String parentPath, String fileName, String newFileType) {
        return changeFileType(parentPath, fileName, newFileType, true);
    }

    /**
     * 修改文件类型
     *
     * @param parentPath 父路径，不需要此项时可为空
     * @param fileName 文件名称
     * @param newFileType 新文件类型
     * @param apply 是否修改本地文件
     * @return 修改后的文件名称, 修改失败则返回空字符串
     * @since 1.0
     */
    public static String changeFileType(String parentPath, String fileName, String newFileType, boolean apply) {
        fileName = getAbsolutePath(parentPath, fileName);
        String newFileName = getFileNameWithoutType(fileName) + TYPE_SPLIT + newFileType;
        changeFileName(parentPath, fileName, newFileName, apply);
        return newFileName;
    }

    /**
     * 获取文件类型，如123.txt将返回txt
     *
     * @param fileName 文件名
     * @return 修改后的文件绝对路径
     * @since 1.0
     */
    public static String getFileType(String fileName) {
        int index = fileName.lastIndexOf(TYPE_SPLIT) + 1;
        return fileName.substring(index);
    }

    /**
     * 获取文件名称，不包含文件类型，如123.txt将返回123
     *
     * @param filePath 文件名
     * @return 文件名称，不包含文件类型
     * @since 1.0
     */
    public static String getFileNameWithoutType(String filePath) {
        String fileName = FilenameUtils.getName(filePath);
        int index = fileName.lastIndexOf(TYPE_SPLIT);
        return index == -1 ? filePath : fileName.substring(0, index);
    }

    /**
     * 判断文件是否存在
     *
     * @param filePath 文件路径
     * @return 存在则返回true，否则为false
     * @since 1.0
     */
    public static boolean isExist(String filePath) {
        return new File(filePath).exists();
    }

    /**
     * 判断文件是否存在
     *
     * @param parentPath 父路径
     * @param filePath 文件路径
     * @return 存在则返回true，否则为false
     * @since 1.0
     */
    public static boolean isExist(String parentPath, String filePath) {
        String realPath = getAbsolutePath(parentPath, filePath);
        return isExist(realPath);
    }

    /**
     * 判断文件是否存在，存在则删除
     * @param filePath 文件路径
     * @since 1.0
     */
    public static void deleteIfExist(String parentPath, String filePath) {
        String realPath = getAbsolutePath(parentPath,filePath);
        deleteIfExist(realPath);
    }

    /**
     * 判断文件是否存在，存在则删除
     * @param filePath 文件路径
     * @since 1.0
     */
    public static void deleteIfExist(String filePath) {
        File file = new File(filePath);
        if (file.exists()) {
            file.delete();
        }
    }

    /**
     * 获取文件
     *
     * @param parentPath 父路径
     * @param filePath 文件路径
     * @return 文件对象
     * @since 1.0
     */
    public static File getFile(String parentPath, String filePath) {
        String realPath = getAbsolutePath(parentPath, filePath);
        return new File(realPath);
    }

    /**
     * 使用UUID和当前时间生成编码后的文件名称，
     *
     * @param filePath 原文件路径
     * @return 编码后的文件名称
     * @since 1.0
     */
    public static String getUUIDFileName(String filePath) {
        String uuid = UUID.randomUUID().toString() + System.currentTimeMillis();
        String fileType = getFileType(filePath);
        return Md5Crypt.md5Crypt(uuid.getBytes()) + TYPE_SPLIT + fileType;
    }
}