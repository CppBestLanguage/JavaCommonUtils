package io.github.pangju666.utils.office;

import io.github.pangju666.utils.sys.FileUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

/**
 * excel 工具包
 *
 * @author 胖橘
 * @version 1.0 2020-12-2
 * @since 1.1
 */
public class ExcelUtils {
    private static final String XLS_TYPE = "xls";
    private static final String XLSX_TYPE = "xlsx";

    protected ExcelUtils() {
    }

    public Workbook getWorkbook(File file) throws IOException {
        String fileType = FileUtils.getFileType(file.getName());
        InputStream stream = FileUtils.openInputStream(file);
        switch (fileType) {
            case XLS_TYPE: return new HSSFWorkbook(stream);
            case XLSX_TYPE: return new XSSFWorkbook(stream);
            default: return null;
        }
    }
}
