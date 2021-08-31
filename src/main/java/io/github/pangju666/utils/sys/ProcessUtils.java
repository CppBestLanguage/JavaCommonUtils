package io.github.pangju666.utils.sys;

import org.apache.commons.io.IOUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.*;
import java.util.function.Consumer;

/**
 * 系统进程工具类
 *
 * @author 胖橘
 * @version 1.0
 * @since 1.0
 */
public class ProcessUtils {
    private static final Runtime SYS_RUNTIME = Runtime.getRuntime();
    private static final ExecutorService EXECUTOR = Executors.newCachedThreadPool();

    /**
     * 系统默认字符编码
     */
    public static final Charset SYS_CHARSET;

    static {
        // 获取系统名称
        String osName = System.getProperty("os.name");
        // 判断是否为window系统
        if (osName.contains("Windows")) {
            SYS_CHARSET = Charset.forName("GB2312");
        } else {
            SYS_CHARSET = StandardCharsets.UTF_8;
        }
    }

    /**
     * 执行系统命令，并使系统默认字符编码获取执行结果，结果从回调获取
     *
     * @param command       系统命令字符串
     * @param data          输入数据，主要用于实现管道输入操作
     * @param charset       命令处理字符集
     * @param inputCallback 执行结果自定义处理
     * @param errorCallback 错误信息自定义处理
     */
    public static void execute(String command, byte[] data, Charset charset,
                               Consumer<InputStream> inputCallback,
                               Consumer<InputStream> errorCallback)
            throws IOException, InterruptedException {
        executeCommand(command, data, charset, inputCallback, errorCallback);
    }

    /**
     * 执行系统命令，并使用指定字符编码获取执行结果
     *
     * @param command 系统命令字符串
     * @param data    输入数据，主要用于实现管道输入操作
     * @param charset 命令处理字符集
     * @return 命令执行结果，执行失败则返回空
     */
    public static String execute(String command, byte[] data, Charset charset)
            throws IOException, InterruptedException {
        return executeCommand(command, data, charset, null, null);
    }

    /**
     * 执行系统命令，并使系统默认字符编码获取执行结果，结果从回调获取
     *
     * @param command       系统命令字符串
     * @param bytes         输入流，主要用于实现管道输入操作
     * @param inputCallback 执行结果自定义处理
     * @param errorCallback 错误信息自定义处理
     */
    public static void execute(String command, byte[] bytes,
                               Consumer<InputStream> inputCallback,
                               Consumer<InputStream> errorCallback)
            throws IOException, InterruptedException {
        execute(command, bytes, SYS_CHARSET, inputCallback, errorCallback);
    }

    /**
     * 执行系统命令，并使用指定字符编码获取执行结果
     *
     * @param command 系统命令字符串
     * @param bytes   输入流，主要用于实现管道输入操作
     * @return 命令执行结果，执行失败则返回空
     */
    public static String execute(String command, byte[] bytes)
            throws IOException, InterruptedException {
        return execute(command, bytes, SYS_CHARSET);
    }

    /**
     * 执行系统命令
     *
     * @param command     系统命令字符串
     * @param inputStream 输入流，主要用于实现管道输入操作
     * @param charset     命令处理字符集
     * @return 命令执行结果，执行失败则返回空
     */
    public static String execute(String command, InputStream inputStream, Charset charset)
            throws IOException, InterruptedException {
        return execute(command, IOUtils.toByteArray(inputStream), charset);
    }

    /**
     * 执行系统命令，并使系统默认字符编码获取执行结果，结果从回调获取
     *
     * @param command       系统命令字符串
     * @param inputStream   输入流，主要用于实现管道输入操作
     * @param charset       命令处理字符集
     * @param inputCallback 执行结果自定义处理
     * @param errorCallback 错误信息自定义处理
     */
    public static void execute(String command, InputStream inputStream, Charset charset,
                               Consumer<InputStream> inputCallback,
                               Consumer<InputStream> errorCallback)
            throws IOException, InterruptedException {
        execute(command, IOUtils.toByteArray(inputStream), charset, inputCallback, errorCallback);
    }

    /**
     * 执行系统命令
     *
     * @param command     系统命令字符串
     * @param inputStream 输入流，主要用于实现管道输入操作
     * @return 命令执行结果，执行失败则返回空
     */
    public static String execute(String command, InputStream inputStream)
            throws IOException, InterruptedException {
        return execute(command, inputStream, SYS_CHARSET);
    }

    /**
     * 执行系统命令，并使系统默认字符编码获取执行结果，结果从回调获取
     *
     * @param command       系统命令字符串
     * @param inputStream   输入流，主要用于实现管道输入操作
     * @param inputCallback 执行结果自定义处理
     * @param errorCallback 错误信息自定义处理
     */
    public static void execute(String command, InputStream inputStream,
                               Consumer<InputStream> inputCallback,
                               Consumer<InputStream> errorCallback)
            throws IOException, InterruptedException {
        execute(command, inputStream, SYS_CHARSET, inputCallback, errorCallback);
    }

    /**
     * 执行系统命令
     *
     * @param command 系统命令字符串
     * @param data    输入数据或参数
     * @param charset 命令处理字符集
     * @return 命令执行结果，执行失败则返回空
     */
    public static String execute(String command, String data, Charset charset)
            throws IOException, InterruptedException {
        return execute(command, data.getBytes(charset), charset);
    }

    /**
     * 执行系统命令，并使系统默认字符编码获取执行结果，结果从回调获取
     *
     * @param command       系统命令字符串
     * @param data          输入数据或参数
     * @param charset       命令处理字符集
     * @param inputCallback 执行结果自定义处理
     * @param errorCallback 错误信息自定义处理
     */
    public static void execute(String command, String data, Charset charset,
                               Consumer<InputStream> inputCallback,
                               Consumer<InputStream> errorCallback)
            throws IOException, InterruptedException {
        execute(command, data.getBytes(charset), SYS_CHARSET, inputCallback, errorCallback);
    }

    /**
     * 执行系统命令，并使用指定字符编码获取执行结果
     *
     * @param command 系统命令字符串
     * @param data    输入数据或参数
     * @return 命令执行结果，执行失败则返回空
     */
    public static String execute(String command, String data) throws IOException, InterruptedException {
        return execute(command, data, SYS_CHARSET);
    }

    /**
     * 执行系统命令，并使系统默认字符编码获取执行结果，结果从回调获取
     *
     * @param command       系统命令字符串
     * @param data          输入数据或参数
     * @param inputCallback 执行结果自定义处理
     * @param errorCallback 错误信息自定义处理
     */
    public static void execute(String command, String data,
                               Consumer<InputStream> inputCallback,
                               Consumer<InputStream> errorCallback)
            throws IOException, InterruptedException {
        execute(command, data, SYS_CHARSET, inputCallback, errorCallback);
    }

    /**
     * 执行系统命令，并使用指定字符编码获取执行结果
     *
     * @param command 系统命令字符串
     * @param charset 命令处理字符集
     * @return 命令执行结果，执行失败则返回空
     */
    public static String execute(String command, Charset charset) throws IOException, InterruptedException {
        return execute(command, "", charset);
    }

    /**
     * 执行系统命令，并使用指定字符编码获取执行结果
     *
     * @param command 系统命令字符串
     * @return 命令执行结果，执行失败则返回空
     */
    public static String execute(String command) throws IOException, InterruptedException {
        return execute(command, SYS_CHARSET);
    }

    /**
     * 执行系统命令，并使系统默认字符编码获取执行结果，结果从回调获取
     *
     * @param command       系统命令字符串
     * @param inputCallback 执行结果自定义处理
     * @param errorCallback 错误信息自定义处理
     */
    public static void execute(String command,
                               Consumer<InputStream> inputCallback,
                               Consumer<InputStream> errorCallback)
            throws IOException, InterruptedException {
        execute(command, "", SYS_CHARSET, inputCallback, errorCallback);
    }

    private static String executeCommand(String command, byte[] data, Charset charset,
                                         Consumer<InputStream> inputCallback,
                                         Consumer<InputStream> errorCallback)
            throws IOException, InterruptedException {
        // 获取命令执行的进程
        Process process = SYS_RUNTIME.exec(command);

        // 判断传入参数是否为空
        if (data != null && data.length != 0) {
            try (OutputStream stream = process.getOutputStream()) {
                String dataStr = new String(data);
                stream.write(dataStr.getBytes(charset));
            }
        }

        // 判断是否由回调处理执行结果
        if (inputCallback != null && errorCallback != null) {
            EXECUTOR.execute(() -> inputCallback.accept(process.getInputStream()));
            EXECUTOR.execute(() -> errorCallback.accept(process.getErrorStream()));
            // 等待进程退出
            process.waitFor();
            // 销毁进程
            process.destroy();
            return null;
        }

        // 异步获取执行结果
        String result = null;
        Future<String> errorFuture = EXECUTOR.submit(() -> IOUtils.toString(process.getErrorStream(), charset));
        Future<String> resultFuture = EXECUTOR.submit(() -> IOUtils.toString(process.getInputStream(), charset));
        // 等待进程退出
        process.waitFor();
        // 判断进程结束标识是否为正常退出
        int exitCode = process.exitValue();
        try {
            if (exitCode == 0) {
                result = resultFuture.get();
            } else {
                result = errorFuture.get();
            }
        } catch (ExecutionException e) {
            e.printStackTrace();
        } finally {
            // 销毁进程
            process.destroy();
        }
        return result;
    }
}
