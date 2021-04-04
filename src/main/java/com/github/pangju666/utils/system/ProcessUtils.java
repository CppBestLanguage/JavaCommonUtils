package com.github.pangju666.utils.system;

import org.apache.commons.io.IOUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * 系统进程工具
 *
 * @author nullptr
 * @version 1.0 2020-9-24
 * @since 1.0 2020-9-24
 */
public class ProcessUtils {
    /** 获取系统运行环境 */
    private static final Runtime SYS_RUNTIME = Runtime.getRuntime();
    /** 系统默认字符编码 */
    public static final Charset SYS_CHARSET;

    private static final Log logger = LogFactory.getLog(ProcessUtils.class);
    private static final ExecutorService EXECUTOR = Executors.newCachedThreadPool();

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
     * 执行系统命令，并使用指定字符编码获取执行结果
     *
     * @param command 系统命令字符串
     * @return 命令执行结果
     * @since 1.0
     */
    public static String execute(String command)
            throws IOException, InterruptedException {
        // 获取命令执行的进程
        Process process = SYS_RUNTIME.exec(command);
        Future<String> future1 = EXECUTOR.submit(()-> IOUtils.toString(process.getInputStream(), SYS_CHARSET));
        Future<String> future2 = EXECUTOR.submit(()-> IOUtils.toString(process.getErrorStream(), SYS_CHARSET));

        process.waitFor();
        int exitCode =  process.exitValue();
        try {
           if (exitCode == 0) {
               return future1.get();
           } else {
               return future2.get();
           }
        } catch (ExecutionException e) {
            e.printStackTrace();
        } finally {
            process.destroy();
        }
        return "";
    }

    /**
     * 执行系统命令
     *
     * @param command 系统命令字符串
     * @param input 输入流，主要用于实现管道输入操作
     * @since 1.0
     */
    public static void execute(String command, InputStream input)
            throws IOException, InterruptedException {
        execute(command, IOUtils.toString(input, SYS_CHARSET));
    }

    /**
     * 执行系统命令
     *
     * @param command 系统命令字符串
     * @param data 输入数据或参数
     * @since 1.0
     */
    public static void execute(String command, String data)
            throws IOException, InterruptedException {
        execute(command, data.getBytes());
    }

    /**
     * 执行系统命令
     *
     * @param command 系统命令字符串
     * @param bytes 输入流，主要用于实现管道输入操作
     * @since 1.0
     */
    public static void execute(String command, byte[] bytes)
            throws IOException, InterruptedException {
        Process process = SYS_RUNTIME.exec(command);
        try(OutputStream stream = process.getOutputStream()) {
            String data = new String(bytes);
            stream.write(data.getBytes(SYS_CHARSET));
        }

        logger.info(IOUtils.toString(process.getInputStream(), SYS_CHARSET));
        logger.warn(IOUtils.toString(process.getErrorStream(), SYS_CHARSET));
        // 等待进程返回并销毁进程
        process.waitFor();
        process.destroy();
    }

    /**
     * 执行系统命令，并使用回调函数处理进程执行结果
     *
     * @param command 系统命令字符串
     * @param inputCallback 执行结果处理接口
     * @param errorCallback 错误信息处理接口
     * @since 1.0
     */
    public static void execute(String command,
                               Function inputCallback,
                               Function errorCallback)
            throws InterruptedException, IOException {
        Process process = SYS_RUNTIME.exec(command);
        // 使用单独的线程来对执行结果输入流与错误输入流进行处理
        EXECUTOR.execute(()-> inputCallback.process(process.getInputStream()));
        EXECUTOR.execute(()-> errorCallback.process(process.getErrorStream()));
        // 等待进程返回并销毁进程
        process.waitFor();
        process.destroy();
    }

    /**
     * 函数式回调
     *
     * @author majl
     * @version 1.0 2020-9-24
     * @since 1.0 2020-9-24
     */
    @FunctionalInterface
    public interface Function {
        /**
         * 回调处理
         *
         * @param stream 输入流
         */
        void process(InputStream stream);
    }
}
