package io.github.utils;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Objects;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.poi.excel.ExcelReader;
import cn.hutool.poi.excel.ExcelUtil;

/**
 * 错误码生成程序
 *
 * @author shenjunyu
 * @since 2023/2/23
 */
public class ErrorCodeGenerateUtils {

    /**
     * 错误码（方法）
     */
    private final static String ERROR_CODE_JAVA_FILE_WITH_ERROR_CODE = "ErrorCodeConstant.java";
    private final static String ERROR_CODE_PROPERTIES_FILE_WITH_ERROR_CODE = "error_code.properties";

    /**
     * 实体类校验错误信息
     */
    private final static String ERROR_CODE_JAVA_FILE_WITHOUT_ERROR_CODE = "ValidErrorMsgConstant.java";

    /**
     * 回车换行
     */
    private final static String ENTER = "\r\n";

    /**
     * Tab
     */
    private final static String TAB = "\t";

    /**
     * 系统名
     */
    private final static String SYSTEM_NAME = "测试系统";

    /**
     * 包名
     */
    private final static String PACKAGE_NAME = "io.github.constant;";

    public static void main(String[] args) {
        // 获取所有数据
        List<List<Object>> dataFromTemplate = getDataFromTemplate();
        // 处理数据
        processDataAndGenerateErrorCodeFile(dataFromTemplate);
    }

    private static void processDataAndGenerateErrorCodeFile(List<List<Object>> dataFromTemplate) {
        if (dataFromTemplate == null || dataFromTemplate.isEmpty()) {
            return;
        }

        StringBuilder errorCodeFileWithErrorCodeJava = new StringBuilder();
        errorCodeFileWithErrorCodeJava.append("package ").append(PACKAGE_NAME).append(ENTER);
        errorCodeFileWithErrorCodeJava.append("public interface ErrorCodeConstant {").append(ENTER).append(ENTER);

        StringBuilder errorCodeFileWithErrorCodeProperties = new StringBuilder();
        errorCodeFileWithErrorCodeProperties.append("# ").append(SYSTEM_NAME).append(ENTER);

        // StringBuilder errorCodeFileWithoutErrorCodeJava = new StringBuilder();
        // errorCodeFileWithoutErrorCodeJava.append("public interface ValidErrorMsgConstant {").append(ENTER)
        // .append(ENTER);

        String identifier = "";
        for (List<Object> row : dataFromTemplate) {
            String type = String.valueOf(row.get(2));
            if (type.contains("方法")) {
                // 导出 error_code.properties 文件
                exportErrorCodeProperties(identifier, errorCodeFileWithErrorCodeProperties, row);

                // 导出 ErrorCodeConstant.java 文件

            } else {
                // 导出 实体类校验错误信息类

            }
        }
    }

    private static boolean exportErrorCodeProperties(String identifier,
        StringBuilder errorCodeFileWithErrorCodeProperties, List<Object> row) {
        // 每行的数据
        String specificBizName = String.valueOf(row.get(1));
        if (!Objects.equals(identifier, specificBizName)) {
            identifier = specificBizName;
            String[] identifiers = identifier.split("-");

            // 导出 properties 文件
            errorCodeFileWithErrorCodeProperties.append("# ").append(identifiers[0]).append(ENTER);
            errorCodeFileWithErrorCodeProperties.append(row.get(4)).append("=").append(row.get(5)).append(ENTER);
        } else {
            // 导出 properties 文件
            errorCodeFileWithErrorCodeProperties.append(row.get(4)).append("=").append(row.get(5)).append(ENTER);
        }

        // 把字符写入文件中
        try {
            OutputStream os = new FileOutputStream("d:/error_code.properties");
            os.write(errorCodeFileWithErrorCodeProperties.toString().getBytes(StandardCharsets.UTF_8));
            if (ObjectUtil.isNotEmpty(os)) {
                os.close();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return false;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    /**
     * 获取模板中所有数据
     *
     * @return
     */
    private static List<List<Object>> getDataFromTemplate() {
        ExcelReader reader = ExcelUtil.getReader(FileUtil.file("error_code_template.xlsx"), 0);
        List<List<Object>> allData = reader.read(2);
        return allData;
    }
}
