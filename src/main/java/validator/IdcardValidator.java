package validator;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.FastDateFormat;

import util.StringUtilsExtend;


/**
 * 
 * 身份证合法性校验器
 * @author gdw
 * @since 1.0
 * 
 * 身份证号码结构:十七位数字本体码(六位数字地址码 + 八位数字出生日期码 + 三位数字顺序码) + 一位校验码组成
 * 地址码(前六位数):
 * 		表示编码对象常住户口所在县(市、旗、区)的行政区划代码,按GB/T2260的规定执行
 * 出生日期码(第七位至十四位):
 * 		表示编码对象出生的年、月、日,按GB/T7408的规定执行
 * 		年、月、日代码之间不用分隔符
 * 顺序码（第十五位至十七位）
 * 		表示在同一地址码所标识的区域范围内,对同年、同月、同日出生的人编定的顺序号,
 * 		顺序码的奇数分配给男性，偶数分配给女性
 * 校验码（第十八位数）
 * 		十七位数字本体码加权求和公式 S = Sum(Ai * Wi), i = 0, ... , 16,先对前17位数字的权求和
 * 			Ai:表示第i位置上的身份证号码数字值
 * 			Wi:表示第i位置上的加权因子,分别是7 9 10 5 8 4 2 1 6 3 7 9 10 5 8 4 2 
 * 		计算模 Y = mod(S, 11)
 * 		通过模得到对应的校验码 Y: 0 1 2 3 4 5 6 7 8 9 10
 * 		 校验码: 1 0 X 9 8 7 6 5 4 3 2
 */
public class IdcardValidator {
	public static boolean validate(String idcard) {
		if(StringUtils.isEmpty(idcard)){
			return false;
		}
		
		if (idcard.length() == 15) {
			idcard = convert15Bit218Bit(idcard);
		}
		return isValidate18Idcard(idcard);
	}
	
	/**
	 * 将15位的身份证转成18位身份证
	 * 
	 * @param idcard
	 * @return
	 */
	private static String convert15Bit218Bit(String idcard) {
		// 非数字,直接返回null
		if(!StringUtils.isNumeric(idcard)){
			return null;
		}
		
		String idcard17 = convert15Bit217Bit(idcard);
		// 生成第18位校验码
		String checkCode = getCheckCode(idcard17);
		
		return idcard17 + checkCode;
	}
	
	/**
	 * 将15位转为17位
	 * 
	 * @param idcard
	 * @return
	 */
	private static String convert15Bit217Bit(String idcard) {
		String birthdayStr = idcard.substring(6, 12);
		Date birthday = null;
		try {
			birthday = FastDateFormat.getInstance("yyMMdd").parse(birthdayStr);
		} catch (ParseException e) {
			throw new RuntimeException(e);
		}
		Calendar cday = Calendar.getInstance();
		cday.setTime(birthday);
		String year = String.valueOf(cday.get(Calendar.YEAR));
		return idcard.substring(0, 6) + year + idcard.substring(8);
	}
	
	/**
	 * 判断是否是正确的18位身份证
	 * @param idcard
	 * @return
	 */
	private static boolean isValidate18Idcard(String idcard) {
		// 非18位为假
		if (idcard.length() != 18) {
			return false;
		}
		return validateCityCode(idcard) && validateBirthday(idcard) && validateCheckCode(idcard);
	}

	/**
	 * 校验城市编码是否正确
	 * @param idcard
	 * @return
	 */
	private static boolean validateCityCode(String idcard) {
		return codeCityMap.containsKey(idcard.substring(0, 2));
	}
	
	/**
	 * 校验出生日期
	 * @param idcard
	 * @return
	 */
	private static boolean validateBirthday(String idcard) {
		Date birthday = getBirthday(idcard);
		return birthday != null && birthday.before(new Date());
	}
	
	private static Date getBirthday(String idcard){
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
		dateFormat.setLenient(false);
		try {
			return dateFormat.parse(idcard.substring(6, 14));
		} catch (ParseException e) {
			return null;
		}
	}
	
	/**
	 * 校验第18位
	 * @param idcard
	 * @return
	 */
	private static boolean validateCheckCode(String idcard) {
		// 获取前17位
		String idcard17 = idcard.substring(0, 17);
		if(StringUtilsExtend.isNotNumeric(idcard17)){
			return false;
		}
		// 生成第18位校验码
		String checkCode = getCheckCode(idcard17);
		// 获取第18位
		String idcard18Code = idcard.substring(17, 18);
		
		// 将身份证的第18位与算出来的校码进行匹配，不相等就为假
		return idcard18Code.equalsIgnoreCase(checkCode);
	}


	/**
	 * 生成第18位的校验码
	 * 
	 * @param idcard17 前17位身份证
	 * @return 校验位
	 */
	public static String getCheckCode(String idcard17) {
		int bit[] = converCharToInt(idcard17.toCharArray());
		int sum17 = getPowerSum(bit);
		return verifyCode[sum17 % verifyCode.length];
	}
	
	/**
	 * 将身份证的每位和对应位的加权因子相乘之后，再得到和值
	 * 
	 * @param bit
	 * @return
	 */
	private static int getPowerSum(int[] bit) {
		int sum = 0;
		if (power.length != bit.length) {
			return sum;
		}
		
		for (int i = 0; i < bit.length; i++) {
			sum += bit[i] * power[i];
		}
		return sum;
	}

	/**
	 * 将字符数组转为整型数组
	 * 
	 * @param chArray
	 * @return
	 */
	private static int[] converCharToInt(char[] chArray) {
		int[] intArray = new int[chArray.length];
		for (int i = 0; i < chArray.length; i++) {
			intArray[i] = chArray[i] - '0';
		}
		return intArray;
	}

	/**
	 * 省，直辖市代码表： { 11:"北京",12:"天津",13:"河北",14:"山西",15:"内蒙古",
	 * 21:"辽宁",22:"吉林",23:"黑龙江",31:"上海",32:"江苏",
	 * 33:"浙江",34:"安徽",35:"福建",36:"江西",37:"山东",41:"河南",
	 * 42:"湖北",43:"湖南",44:"广东",45:"广西",46:"海南",50:"重庆",
	 * 51:"四川",52:"贵州",53:"云南",54:"西藏",61:"陕西",62:"甘肃",
	 * 63:"青海",64:"宁夏",65:"新疆",71:"台湾",81:"香港",82:"澳门",91:"国外"}
	 */
	private static HashMap<String, String> codeCityMap = new HashMap<>();
	static{
		codeCityMap.put("11", "北京");
        codeCityMap.put("12", "天津");
        codeCityMap.put("13", "河北");
        codeCityMap.put("14", "山西");
        codeCityMap.put("15", "内蒙古");
        
        codeCityMap.put("21", "辽宁");
        codeCityMap.put("22", "吉林");
        codeCityMap.put("23", "黑龙江");
        
        codeCityMap.put("31", "上海");
        codeCityMap.put("32", "江苏");
        codeCityMap.put("33", "浙江");
        codeCityMap.put("34", "安徽");
        codeCityMap.put("35", "福建");
        codeCityMap.put("36", "江西");
        codeCityMap.put("37", "山东");
        
        codeCityMap.put("41", "河南");
        codeCityMap.put("42", "湖北");
        codeCityMap.put("43", "湖南");
        codeCityMap.put("44", "广东");
        codeCityMap.put("45", "广西");
        codeCityMap.put("46", "海南");
        
        codeCityMap.put("50", "重庆");
        codeCityMap.put("51", "四川");
        codeCityMap.put("52", "贵州");
        codeCityMap.put("53", "云南");
        codeCityMap.put("54", "西藏");
        
        codeCityMap.put("61", "陕西");
        codeCityMap.put("62", "甘肃");
        codeCityMap.put("63", "青海");
        codeCityMap.put("64", "宁夏");
        codeCityMap.put("65", "新疆");
        
        codeCityMap.put("71", "台湾");
        
        codeCityMap.put("81", "香港");
        codeCityMap.put("82", "澳门");
        
        codeCityMap.put("91", "国外");
	}

	// 每位加权因子
	private static int power[] = { 7, 9, 10, 5, 8, 4, 2, 1, 6, 3, 7, 9, 10, 5, 8, 4, 2 };

	// 第18位校检码
	private static String verifyCode[] = { "1", "0", "X", "9", "8", "7", "6", "5", "4", "3", "2" };
}
