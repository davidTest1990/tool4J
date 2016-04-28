package validator;

import org.junit.Test;
import static org.junit.Assert.*;

public class IdcardValidatorTest {
	@Test
	public void falseCityCode(){
		String idcard17 = "382322" + "19900412" + "234";
		assertFalse(IdcardValidator.validate(idcard17 + IdcardValidator.getCheckCode(idcard17)));
	}
	
	@Test
	public void falseDate(){
		String idcard17 = "342401" + "19900466" + "234";
		assertFalse(IdcardValidator.validate(idcard17 + IdcardValidator.getCheckCode(idcard17)));
	}
	
	@Test
	public void falseUnborn(){
		String idcard17 = "342401" + "20900401" + "234";
		assertFalse(IdcardValidator.validate(idcard17 + IdcardValidator.getCheckCode(idcard17)));
	}

	@Test
	public void falseCheckCode(){
		String idcard17 = "342401" + "20900401" + "234" + 1;
		assertFalse(IdcardValidator.validate(idcard17));
	}

	@Test
	public void trueIdcard(){
		String idcard17 = "342401" + "19900401" + "234";
		assertTrue(IdcardValidator.validate(idcard17 + IdcardValidator.getCheckCode(idcard17)));
	}
	
	@Test
	public void true15Idcard(){
		assertTrue(IdcardValidator.validate("342401900714192"));
	}
}
