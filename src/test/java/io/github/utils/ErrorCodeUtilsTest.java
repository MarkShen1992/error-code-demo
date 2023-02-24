package io.github.utils;

import io.github.constant.ErrorCodeConstant;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * ErrorCodeUtils测试用例
 *
 * @author shenjunyu
 * @email sjy13149@cnki.net
 * @since 2023/2/24 11:09
 */
@SpringBootTest
public class ErrorCodeUtilsTest {

    @Test
    public void getValue01() {
        String errMsg = ErrorCodeUtils.getValue(ErrorCodeConstant.Common.GET_DATA_SUCCESS);
        assertEquals("获取数据成功", errMsg);
    }

    @Test
    public void getValue02() {
        String errMsg = ErrorCodeUtils.getValue(ErrorCodeConstant.User.USERNAME_CAN_NOT_BE_REPEATED, "张三");
        assertEquals("用户名张三不可以重复", errMsg);
    }
}
