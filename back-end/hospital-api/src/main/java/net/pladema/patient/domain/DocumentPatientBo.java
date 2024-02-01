package net.pladema.patient.domain;

import ar.lamansys.sgh.shared.domain.general.ContactInfoBo;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DocumentPatientBo {

	private String firstName;

	private String middleName;

	private String lastName;

	private String otherLastName;

	private String selfPerceivedName;

	private ContactInfoBo contactInfo;

	private String selfPerceivedGender;

	public DocumentPatientBo(String firstName, String middleName, String lastName, String otherLastName, String selfPerceivedName, String email, String selfPerceivedGender,
							 String phonePrefix, String phoneNumber, String street, String number, String floor, String apartment, String cityName, String stateName) {
		this.firstName = firstName;
		this.middleName = middleName;
		this.lastName = lastName;
		this.otherLastName = otherLastName;
		this.selfPerceivedName = selfPerceivedName;
		this.selfPerceivedGender = selfPerceivedGender;
		contactInfo =  new ContactInfoBo(phonePrefix, phoneNumber, street, number, floor, apartment, cityName, stateName, email);
	}
}
