package io.github.dto;

import io.github.constant.ValidErrorMsgConstant;

import javax.validation.constraints.NotBlank;

/**
 * @author shenjunyu
 * @email sjy13149@cnki.net
 * @since 2023/2/24 11:40
 */
public class UserDto {

    @NotBlank(message = ValidErrorMsgConstant.User.USERNAME_CAN_NOT_BE_BLANK)
    private String userName;

    private String password;
}
