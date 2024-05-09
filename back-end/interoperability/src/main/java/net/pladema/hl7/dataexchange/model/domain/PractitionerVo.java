package net.pladema.hl7.dataexchange.model.domain;

import lombok.Getter;
import lombok.Setter;
import net.pladema.hl7.dataexchange.model.adaptor.FhirAddress;

import net.pladema.hl7.dataexchange.model.adaptor.FhirDateMapper;
import net.pladema.hl7.dataexchange.model.adaptor.FhirString;

import org.apache.commons.collections4.BidiMap;
import org.apache.commons.collections4.bidimap.DualHashBidiMap;

import java.time.LocalDate;
import java.util.Date;

@Getter
@Setter
public class PractitionerVo {

	public static final String COUNTRY="AR";

	private final BidiMap<Short, String> genderCoding = new DualHashBidiMap<>();

	public PractitionerVo(){
		genderCoding.put((short)1, "female");
		genderCoding.put((short)2, "male");
	}

	public PractitionerVo(Integer id, String identificationNumber, String firstName, String middleNames, String lastName, String otherLastNames,
						  String phonePrefix, String phoneNumber, Integer addressId, Short genderId,
						  Date birthDate) {
		this.id = id.toString();
		this.identificationNumber = identificationNumber;
		this.firstName = firstName;
		this.middleNames = middleNames;
		this.lastName = lastName;
		this.otherLastNames = otherLastNames;
		this.phonePrefix = phonePrefix;
		this.phoneNumber = phoneNumber;
		this.addressId = addressId;
		setGender(genderCoding.getOrDefault(genderId, null));
		setBirthDate(FhirDateMapper.toLocalDate(birthDate));
	}

	private String id;
	private String identificationNumber;
	private String refeps;
	private String textName;
	private String firstName;
	private String middleNames;
	private String lastName;
	private String otherLastNames;
	//private String telecom; --> 	A contact detail for the practitioner (that apply to all roles)
	private String phonePrefix;
	private String phoneNumber;
	private Integer addressId;
	private FhirAddress fullAddress;
	private Short gender;
	private LocalDate birthDate;
	//private String photo????
	// qualification --> Certification, licenses, or training pertaining to the provision of care
		//identifier
		//code
		//period --> Period during which the qualification is valid
		//issuer (organization) --> Organization that regulates and issues the qualification
	//private String communication

	public String getGender(){
		return genderCoding.get(gender);
	}

	public void setGender(String gender){
		this.gender = genderCoding.getKey(gender);
	}

	public String getStringBirthDate() {
		return birthDate.toString();
	}

	public String getFullName(){
		return FhirString.joining(lastName, otherLastNames, firstName, middleNames);
	}
}
