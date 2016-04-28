package util;

import org.apache.commons.lang3.StringUtils;

/**
 * {@link org.apache.commons.lang3.StringUtils}的扩展
 * @author gdw
 * @since 1.0
 *
 */
public class StringUtilsExtend {
	public static boolean isNotNumeric(CharSequence cs){
		return !StringUtils.isNumeric(cs);
	}
}
