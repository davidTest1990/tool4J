package validator;

import static org.junit.Assert.*;

import org.junit.Test;

public class BankCardValidatorTest {
	@Test
	public void trueBankCard(){
		assertTrue(BankCardValidator.validate("6212288802000001666"));
	}
	
	@Test
	public void falseBankCard(){
		assertFalse(BankCardValidator.validate("6212288802000002666"));
	}
}
