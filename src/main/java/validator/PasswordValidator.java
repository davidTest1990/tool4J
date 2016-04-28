package validator;

import org.apache.commons.lang3.StringUtils;

/**
 * 密码验证器
 * 
 * @author gdw
 * @since 1.0
 */
public class PasswordValidator {
	public static boolean validate(String password) {
		if (StringUtils.isEmpty(password)) {
			return false;
		}
		// 自定义的密码校验规则
		return password.length() >= 6 && password.length() <= 15;
	}
}
