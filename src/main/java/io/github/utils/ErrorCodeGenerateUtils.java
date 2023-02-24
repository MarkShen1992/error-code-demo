package io.github.utils;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import org.apache.poi.util.StringUtil;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.poi.excel.ExcelReader;
import cn.hutool.poi.excel.ExcelUtil;

/**
 * 错误码生成程序
 *
 * @author shenjunyu
 * @email sjy13149@cnki.net
 * @since 2023/2/23 16:21
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
    private final static String SYSTEM_NAME = "XX系统";

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

        // 导出 error_code.properties 文件
        exportErrorCodeProperties(dataFromTemplate);

        // 导出 ErrorCodeConstant.java 文件
        StringBuilder errorCodeFileWithErrorCodeJava = new StringBuilder();
        errorCodeFileWithErrorCodeJava.append("package ").append(PACKAGE_NAME).append(ENTER).append(ENTER);
        errorCodeFileWithErrorCodeJava.append("/**").append(ENTER);
        errorCodeFileWithErrorCodeJava.append(" * ").append("错误码常量类").append(ENTER);
        errorCodeFileWithErrorCodeJava.append(" */").append(ENTER);
        errorCodeFileWithErrorCodeJava.append("public interface ErrorCodeConstant {").append(ENTER);

        // 导出 ValidErrorMsgConstant.java 文件
        StringBuilder errorCodeFileWithoutErrorCodeJava = new StringBuilder();
        errorCodeFileWithoutErrorCodeJava.append("package ").append(PACKAGE_NAME).append(ENTER).append(ENTER);
        errorCodeFileWithoutErrorCodeJava.append("/**").append(ENTER);
        errorCodeFileWithoutErrorCodeJava.append(" * ").append("实体类验证错误信息").append(ENTER);
        errorCodeFileWithoutErrorCodeJava.append(" */").append(ENTER);
        errorCodeFileWithoutErrorCodeJava.append("public interface ValidErrorMsgConstant {").append(ENTER);

        Map<Object, List<List<Object>>> dataMap =
            dataFromTemplate.stream().collect(Collectors.groupingBy(data -> data.get(1)));
        // Map 遍历的四种方式: http://c.biancheng.net/view/6872.html
        for (Map.Entry<Object, List<List<Object>>> entry : dataMap.entrySet()) {
            String key = String.valueOf(entry.getKey());
            List<List<Object>> value = entry.getValue();
            if (ObjectUtil.isNotEmpty(value)) {
                List<List<Object>> functionDataList =
                    value.stream().filter(v -> String.valueOf(v.get(2)).contains("方法")).collect(Collectors.toList());
                if (ObjectUtil.isNotEmpty(functionDataList)) {
                    buildInnerInterface(errorCodeFileWithErrorCodeJava, functionDataList, key, key.split("-")[0]);
                }
                List<List<Object>> modelDataList =
                    value.stream().filter(v -> String.valueOf(v.get(2)).contains("实体类")).collect(Collectors.toList());
                if (ObjectUtil.isNotEmpty(modelDataList)) {
                    buildInnerInterface(errorCodeFileWithoutErrorCodeJava, modelDataList, key, key.split("-")[0]);
                }
            }
        }
        errorCodeFileWithErrorCodeJava.append("}");
        writeStringToTheDisk(errorCodeFileWithErrorCodeJava, "d:/" + ERROR_CODE_JAVA_FILE_WITH_ERROR_CODE);

        errorCodeFileWithoutErrorCodeJava.append("}");
        writeStringToTheDisk(errorCodeFileWithoutErrorCodeJava, "d:/" + ERROR_CODE_JAVA_FILE_WITHOUT_ERROR_CODE);
    }

    /**
     * 导出 error_code.properties 到文件中
     *
     * @param dataFromTemplate
     */
    private static void exportErrorCodeProperties(List<List<Object>> dataFromTemplate) {
        StringBuilder errorCodeFileWithErrorCodeProperties = new StringBuilder();
        errorCodeFileWithErrorCodeProperties.append("# ").append(SYSTEM_NAME).append(ENTER);
        String identifier = "";
        for (List<Object> row : dataFromTemplate) {
            String type = String.valueOf(row.get(2));
            String specificBizName = String.valueOf(row.get(1));
            String[] identifiers = specificBizName.split("-");
            if (type.contains("方法")) {
                // 导出 error_code.properties 文件
                if (!Objects.equals(identifier, specificBizName)) {
                    identifier = specificBizName;

                    // 导出 properties 文件
                    errorCodeFileWithErrorCodeProperties.append("# ").append(identifiers[0]).append(ENTER);
                    errorCodeFileWithErrorCodeProperties.append(row.get(4)).append("=").append(row.get(5))
                        .append(ENTER);
                } else {
                    // 导出 properties 文件
                    errorCodeFileWithErrorCodeProperties.append(row.get(4)).append("=").append(row.get(5))
                        .append(ENTER);
                }
            }
        }
        // error_code.properties
        writeStringToTheDisk(errorCodeFileWithErrorCodeProperties, "d:/" + ERROR_CODE_PROPERTIES_FILE_WITH_ERROR_CODE);
    }

    private static void buildInnerInterface(StringBuilder sb, List<List<Object>> rows, String key,
        String interfaceName) {

        sb.append(ENTER);
        // 导出 实体类校验错误信息类
        sb.append(TAB).append("/**").append(ENTER);
        sb.append(TAB).append(" * ").append(key).append(ENTER);
        sb.append(TAB).append(" */").append(ENTER);
        sb.append(TAB).append("interface ").append(interfaceName).append(" {").append(ENTER).append(ENTER);

        for (List<Object> row : rows) {
            sb.append(TAB).append(TAB).append("/**").append(ENTER);
            sb.append(TAB).append(TAB).append(" * ").append(row.get(5)).append(ENTER);
            sb.append(TAB).append(TAB).append(" */").append(ENTER);
            sb.append(TAB).append(TAB).append("String ").append(row.get(3)).append(" ").append("=").append(" ")
                .append("\"").append(String.valueOf(row.get(2)).equals("实体类") ? row.get(5) : row.get(4)).append("\"")
                .append(";").append(ENTER).append(ENTER);
        }

        sb.append(TAB).append("}").append(ENTER);
    }

    /**
     * 将字符串写入到文件中
     *
     * @param sb
     *            字符串
     * @param fileAbsolutePath
     *            文件绝对路径, 如Windows中路径d:/1.txt
     */
    public static void writeStringToTheDisk(StringBuilder sb, String fileAbsolutePath) {
        if (StringUtil.isBlank(fileAbsolutePath)) {
            return;
        }

        // 把字符写入文件中
        try {
            OutputStream os = new FileOutputStream(fileAbsolutePath);
            os.write(sb.toString().getBytes(StandardCharsets.UTF_8));
            if (ObjectUtil.isNotEmpty(os)) {
                os.close();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取模板中所有数据
     *
     * @return
     */
    private static List<List<Object>> getDataFromTemplate() {
        ExcelReader reader = ExcelUtil.getReader(FileUtil.file("error_code_template.xlsx"), 0);
        List<List<Object>> allData = reader.read(2);
        if (ObjectUtil.isNotNull(reader)) {
            reader.close();
        }
        return allData;
    }
}
