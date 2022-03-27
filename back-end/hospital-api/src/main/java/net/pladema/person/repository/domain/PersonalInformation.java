package net.pladema.person.repository.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.pladema.address.repository.entity.Address;
import net.pladema.address.repository.entity.City;
import net.pladema.address.repository.entity.Province;
import net.pladema.person.repository.entity.IdentificationType;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
public class PersonalInformation {

	private Integer id;

	private String identificationNumber;

	private LocalDate birthDate;

	private String email;

	private String cuil;

	private String phonePrefix;

	private String phoneNumber;

	private IdentificationType identificationType;

	private Address address;

	private City city;

	private Province province;

	public PersonalInformation(Integer personId, String identificationNumber, LocalDate birthDate, String email,
			Short identificationTypeId, String identificationTypeDesc, String cuil, String phonePrefix, String phoneNumber,
			Integer addressId, String street, String number, String floor, String apartment, Integer cityId,
			String city, Short provinceId, String province) {
		this.id = personId;
		this.identificationNumber = identificationNumber;
		this.birthDate = birthDate;
		this.email = email;
		this.phonePrefix = phonePrefix;
		this.phoneNumber = phoneNumber;
		this.cuil = cuil;

		this.identificationType = new IdentificationType(identificationTypeId, identificationTypeDesc);

		this.address = new Address();
		this.address.setId(addressId);
		this.address.setStreet(street);
		this.address.setNumber(number);
		this.address.setFloor(floor);
		this.address.setApartment(apartment);

		this.city = new City();
		this.city.setId(cityId);
		this.city.setDescription(city);

		this.province = new Province();
		this.province.setId(provinceId);
		this.province.setDescription(province);
	}
}
