package net.pladema.cipres.infrastructure.output.rest.person;

import ar.lamansys.sgh.shared.infrastructure.input.service.SharedPersonPort;
import ar.lamansys.sgh.shared.infrastructure.input.service.person.CompletePersonDto;
import lombok.RequiredArgsConstructor;
import net.pladema.cipres.application.port.CipresPersonStorage;

import net.pladema.cipres.domain.PersonDataBo;

import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class CipresPersonStorageImpl implements CipresPersonStorage {

	private final SharedPersonPort sharedPersonPort;

	@Override
	public PersonDataBo getPersonData(Integer personId) {
		return mapToPersonDataBo(sharedPersonPort.getCompletePersonData(personId));
	}

	private PersonDataBo mapToPersonDataBo(CompletePersonDto personData) {
		return PersonDataBo.builder()
				.firstName(personData.getFirstName())
				.middleNames(personData.getMiddleNames())
				.lastName(personData.getLastName())
				.otherLastNames(personData.getOtherLastNames())
				.identificationTypeId(personData.getIdentificationTypeId())
				.identificationNumber(personData.getIdentificationNumber())
				.genderId(personData.getGenderId())
				.birthDate(personData.getBirthDate())
				.phonePrefix(personData.getPhonePrefix())
				.phoneNumber(personData.getPhoneNumber())
				.email(personData.getEmail())
				.street(personData.getStreet())
				.number(personData.getNumber())
				.floor(personData.getFloor())
				.apartment(personData.getApartment())
				.quarter(personData.getQuarter())
				.postcode(personData.getPostcode())
				.cityBahraCode(personData.getCityBahraCode())
				.department(personData.getDepartment())
				.city(personData.getCity())
				.country(personData.getCountry())
				.build();
	}

}
