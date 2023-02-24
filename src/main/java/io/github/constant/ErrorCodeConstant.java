package io.github.constant;

/**
 * 错误码常量类
 */
public interface ErrorCodeConstant {

	/**
	 * Common-公共错误信息
	 */
	interface Common {

		/**
		 * 获取数据成功
		 */
		String GET_DATA_SUCCESS = "ERROR_CODE_00000";

		/**
		 * 获取数据失败
		 */
		String GET_DATA_FAILURE = "ERROR_CODE_00001";

	}

	/**
	 * User-用户错误信息
	 */
	interface User {

		/**
		 * 用户名不可以重复
		 */
		String USERNAME_CAN_NOT_BE_REPEATED = "ERROR_CODE_00100";

	}

	/**
	 * Role-角色错误信息
	 */
	interface Role {

		/**
		 * 角色不存在
		 */
		String ROLE_NOT_EXIST = "ERROR_CODE_00200";

	}
}