package io.github.pangju666.utils;

import io.github.pangju666.utils.io.FileUtils;
import org.junit.jupiter.api.Test;

import java.io.File;

public class FileUtilsTest {

    @Test
    public void getMineTypeTest() {
        File file = new File("C:\\Users\\OriginAI-21041703\\Desktop\\表单下载.txt");
        String mineType = FileUtils.getMineType(file);
        System.out.println(mineType);
    }
}
