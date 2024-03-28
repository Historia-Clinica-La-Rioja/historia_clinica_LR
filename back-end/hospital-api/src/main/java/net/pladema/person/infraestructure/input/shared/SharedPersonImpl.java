package net.pladema.person.infraestructure.input.shared;

import ar.lamansys.sgh.shared.domain.general.ContactInfoBo;
import ar.lamansys.sgh.shared.infrastructure.input.service.person.CompletePersonDto;

import net.pladema.address.repository.entity.Address;
import net.pladema.address.repository.entity.City;
import net.pladema.address.repository.entity.Country;
import net.pladema.person.repository.domain.CompletePersonVo;
import net.pladema.person.repository.entity.Person;
import net.pladema.person.repository.entity.PersonExtended;

import org.springframework.stereotype.Service;

import ar.lamansys.sgh.shared.infrastructure.input.service.SharedPersonPort;
import ar.lamansys.sgx.shared.exceptions.NotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.pladema.hl7.dataexchange.model.adaptor.FhirString;
import net.pladema.person.service.PersonService;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class SharedPersonImpl implements SharedPersonPort {

	private final PersonService personService;

	@Override
	public String getCountryIsoCodeFromPerson(Integer personId) {
		return personService.getCountryIsoCodeFromPerson(personId);
	}

	@Override
	public String getPersonFullNameById(Integer personId) {
		log.debug("Input paremeters -> personId {}", personId);
		return personService.findPerson(personId)
				.map(person -> FhirString.joining(person.getFirstName(), person.getMiddleNames(), person.getLastName(), person.getOtherLastNames()))
				.orElseThrow(()-> new NotFoundException("person-not-found", String.format("La persona con id %s no existe", personId)));
	}

	@Override
	public String getCompletePersonNameById(Integer personId) {
		log.debug("Input paremeters -> personId {}", personId);
		return personService.getCompletePersonNameById(personId);
	}

	@Override
	public String parseCompletePersonName(String firstName, String middleNames, String lastName, String otherLastNames, String selfDeterminateName) {
		log.debug("Input paremeters -> firstName {}, middleNames {}, lastName {}, otherLastNames {}, selfDeterminationName {} ",
				firstName, middleNames, lastName, otherLastNames, selfDeterminateName);
		return personService.parseCompletePersonName(firstName, middleNames, lastName, otherLastNames, selfDeterminateName);
	}

	@Override
	public String getFormalPersonNameById(Integer personId) {
		log.debug("Input paremeters -> personId {}", personId);
		return personService.getFormalPersonNameById(personId);
	}

	@Override
	public ContactInfoBo getPersonContactInfoById(Integer personId) {
		log.debug("Input paremeters -> personId {}", personId);
		ContactInfoBo result = personService.getContactInfoById(personId);
		log.debug("Output -> {}", result);
		return result;
	}

	@Override
	public List<String> getCompletePersonsNameByIds(List<Integer> personIds) {
		log.debug("Input parameters -> personIds {}", personIds);
		List<String> result = personService.getCompletePersonNameByIds(personIds);
		log.debug("Output -> {}", result);
		return result;
	}

	public CompletePersonDto getCompletePersonData(Integer personId) {
		return personService.getCompletePerson(personId)
				.map(this::mapToCompletePersonData)
				.orElseThrow(()-> new NotFoundException("person-not-found", String.format("La persona con id %s no existe", personId)));
	}

	private CompletePersonDto mapToCompletePersonData(CompletePersonVo completePerson) {
		Person person = completePerson.getPerson();
		PersonExtended personExtended = completePerson.getPersonExtended();
		Address address = completePerson.getAddress();
		City city = completePerson.getCity();
		Country country = completePerson.getCountry();
		return CompletePersonDto.builder()
				.firstName(person.getFirstName())
				.middleNames(person.getMiddleNames())
				.lastName(person.getLastName())
				.otherLastNames(person.getOtherLastNames())
				.identificationTypeId(person.getIdentificationTypeId())
				.identificationNumber(person.getIdentificationNumber())
				.genderId(person.getGenderId())
				.birthDate(person.getBirthDate())
				.phonePrefix(personExtended.getPhonePrefix())
				.phoneNumber(personExtended.getPhoneNumber())
				.email(personExtended.getEmail())
				.street(address.getStreet())
				.number(address.getNumber())
				.floor(address.getFloor())
				.apartment(address.getApartment())
				.quarter(address.getQuarter())
				.postcode(address.getPostcode())
				.department(completePerson.getDepartment() != null ? completePerson.getDepartment().getDescription() : null)
				.city(city != null ? city.getDescription() : null)
				.cityBahraCode(city != null ? city.getBahraCode() : null)
				.country(country != null ? country.getDescription() : null)
				.build();
	}
}
