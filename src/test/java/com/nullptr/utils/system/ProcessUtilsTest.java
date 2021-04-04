package com.nullptr.utils.system;


import org.junit.Test;

import java.io.IOException;

public final class ProcessUtilsTest {
    @Test
    public void test() {
        try {
            // 返回值是执行结果
            String result = ProcessUtils.execute("安装目录/program/soffice --headless --convert-to pdf 源路径 --outdir 输出路径");
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }
}
