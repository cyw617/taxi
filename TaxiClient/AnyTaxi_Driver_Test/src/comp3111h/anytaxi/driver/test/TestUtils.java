package comp3111h.anytaxi.driver.test;

import com.appspot.hk_taxi.anyTaxi.model.Driver;
import com.appspot.hk_taxi.anyTaxi.model.PhoneNumber;

public class TestUtils {

	static public Driver driver;
	
	public static Driver getDriver() {		
		driver = new Driver();
		driver.setEmail("yibairen1994@gmail.com");
		driver.setName("Byron");
		PhoneNumber pn = new PhoneNumber();
		pn.setNumber("54251109");
		driver.setPhoneNumber(pn);
		driver.setLicense("testLicense");
		
		return driver;
	}

}
