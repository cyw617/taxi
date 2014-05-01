package comp3111h.anytaxi.customer.test;

import com.appspot.hk_taxi.anyTaxi.model.Customer;
import com.appspot.hk_taxi.anyTaxi.model.PhoneNumber;

public class TestUtils {

	static public Customer customer;
	
	public static Customer getCustomer() {		
		customer = new Customer();
		customer.setEmail("ywangbc@gmail.com");
		customer.setName("Byron");
		PhoneNumber pn = new PhoneNumber();
		pn.setNumber("54251109");
		customer.setPhoneNumber(pn);
		
		return customer;
	}

}
