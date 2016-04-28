package validator;

import util.StringUtilsExtend;

/**
 * 银行卡卡号校验器
 * @author gdw
 * @since 1.0
 * 
 * 国内的银行卡号是一串根据Luhm校验算法计算出来的数字
 * Luhm校验规则:16位银行卡号（19位通用）
 * 		将未带校验位的 15（或18）位卡号从右依次编号 1 到 15（18），位于奇数位号上的数字乘以 2。
 * 		将奇位乘积的个十位全部相加，再加上所有偶数位上的数字。
 *		将加法和加上校验位能被 10 整除。
 */
public class BankCardValidator {
	public static boolean validate(String bankCard) {
        if(StringUtilsExtend.isNotNumeric(bankCard)) {
            return false;
        }
		char checkCode = getCheckCode(bankCard);
        return bankCard.charAt(bankCard.length() - 1) == checkCode;
	}
	
	/**
	 * 获取最后一位校验位
	 * @param bankCard
	 * @return
	 */
	private static char getCheckCode(String bankCard){
        char[] chs = bankCard.toCharArray();
        int luhmSum = 0;
        for(int i = chs.length - 2; i >= 0; i--) {
            int k = chs[i] - '0';
            if((i & 1) != 0) {
                k *= 2;
                k = k / 10 + k % 10;
            }
            luhmSum += k;           
        }
        return (luhmSum % 10 == 0) ? '0' : (char)((10 - luhmSum % 10) + '0');
    }
}
