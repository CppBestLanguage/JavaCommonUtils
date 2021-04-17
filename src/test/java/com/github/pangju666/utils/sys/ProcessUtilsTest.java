package com.github.pangju666.utils.sys;


import com.github.pangju666.utils.security.VerificationImageUtils;
import org.apache.commons.io.IOUtils;
import org.junit.jupiter.api.Test;

import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;

public final class ProcessUtilsTest {
    @Test
    public void test() {
        try {
            // 返回值是执行结果
            ProcessUtils.execute("tasklist", (inputStream)-> {
                try {
                    System.out.println(IOUtils.toString(inputStream, ProcessUtils.SYS_CHARSET));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }, (errorStream)-> {
                try {
                    System.out.println(IOUtils.toString(errorStream, ProcessUtils.SYS_CHARSET));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void test1() throws IOException {
        ImageIO.write(new VerificationImageUtils().generate("1227"), "png", new File("C:\\Users\\胖橘\\Desktop\\demo.png"));
    }
}
