package validator;

/**
 * 常见校验器
 * @author gdw
 * @since 1.0
 *
 */
public class GeneralValidator {
	/**
	 * 判断email格式是否正确
	 * 
	 * @param email
	 * @return
	 */
	public static boolean isEmail(String email){
		return email != null && email.matches("\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*");
	}
	
	/**
	 * 判断手机号码
	 * 
	 * @param mobile
	 * @return
	 */
	public static boolean isMobile(String mobile){
		return mobile != null && mobile.matches("^[1]([3,4,5,7,8][0-9]{1})[0-9]{8}$");
	}
	
	/**
	 * 验证是否是中文字符
	 * @param s
	 * @return
	 */
    public static boolean isChinese(String s) { 
        return s !=null && s.matches("[\\u4E00-\\u9FBF]+"); 
    } 
    
	/**
	 * 验证IP格式
	 * 
	 * @param ip
	 * @return
	 */
	public static boolean isIp(String ip){
		String regex = "((2[0-4]\\d)|(25[0-5]))|(1\\d{2})|([1-9]\\d)|(\\d)";
		regex = "(" + regex + ").(" + regex + ").(" + regex + ").(" + regex + ")";
		return ip != null && ip.matches(regex);
	}
}
