package ar.lamansys.sgh.shared.domain.general;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ContactInfoBo {

	private PhoneBo phone;

	private AddressBo address;

	private String email;

	public ContactInfoBo(String phonePrefix, String phoneNumber, String street, String number, String floor, String apartment, String cityName, String stateName, String email) {
		phone = new PhoneBo(phonePrefix, phoneNumber);
		address = new AddressBo(street, number, floor, apartment, cityName, stateName);
		this.email = email;
	}

}
