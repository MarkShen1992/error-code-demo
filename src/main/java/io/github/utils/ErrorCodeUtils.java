package io.github.utils;

import java.io.IOException;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.EncodedResource;
import org.springframework.core.io.support.PropertiesLoaderUtils;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;

import io.github.constant.EncodeConstant;

/**
 * 错误码配套工具类
 * 
 * @author shenjunyu
 * @since 2023/2/23
 */
public class ErrorCodeUtils {

    /**
     * error code
     */
    private static final String ERROR_CODE = "error_code";

    private static final String ERROR_CODE_PROPERTIES = "error_code.properties";

    /**
     * 日志
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(ErrorCodeUtils.class);

    /**
     * cache
     */
    private static final Cache<Object, Object> PROPERTIES_CACHE = CacheBuilder.newBuilder().build();

    static {
        buildProperties();
    }

    /**
     * 构建 Properties
     *
     * @return
     */
    private static void buildProperties() {
        Resource resource = new ClassPathResource(ERROR_CODE_PROPERTIES);
        EncodedResource encodedResource = new EncodedResource(resource, EncodeConstant.UTF8);
        Properties props = new Properties();
        try {
            props = PropertiesLoaderUtils.loadProperties(encodedResource);
        } catch (IOException e) {
            LOGGER.error(e.getMessage());
        }
        PROPERTIES_CACHE.put(ERROR_CODE, props);
    }

    /**
     * 获取错误信息
     *
     * @param errorCode
     * @return
     */
    public static String getValue(String errorCode) {
        Properties props = (Properties)PROPERTIES_CACHE.getIfPresent(ERROR_CODE);
        return props.getProperty(errorCode);
    }

    /**
     * 获取错误信息
     *
     * @param errorCode
     * @param objects
     * @return
     */
    public static String getValue(String errorCode, Object... objects) {
        Properties props = (Properties)PROPERTIES_CACHE.getIfPresent(ERROR_CODE);
        String value = props.getProperty(errorCode);
        for (int i = 0; i < objects.length; i++) {
            value = value.replace("{" + i + "}", String.valueOf(objects[i]));
        }
        return value;
    }
}
