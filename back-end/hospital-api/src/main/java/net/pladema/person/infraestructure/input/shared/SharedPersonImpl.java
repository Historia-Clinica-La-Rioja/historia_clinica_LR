package net.pladema.person.infraestructure.input.shared;

import ar.lamansys.sgh.shared.domain.general.ContactInfoBo;
import ar.lamansys.sgh.shared.infrastructure.input.service.person.CompletePersonDto;

import ar.lamansys.sgh.shared.infrastructure.input.service.person.PersonDto;
import net.pladema.address.repository.entity.Address;
import net.pladema.address.repository.entity.City;
import net.pladema.address.repository.entity.Country;
import net.pladema.person.controller.mapper.PersonMapper;
import net.pladema.person.repository.domain.CompletePersonVo;
import net.pladema.person.repository.entity.Person;
import net.pladema.person.repository.entity.PersonExtended;

import net.pladema.user.domain.PersonBo;

import org.springframework.stereotype.Service;

import ar.lamansys.sgh.shared.infrastructure.input.service.SharedPersonPort;
import ar.lamansys.sgx.shared.exceptions.NotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.pladema.hl7.dataexchange.model.adaptor.FhirString;
import net.pladema.person.service.PersonService;

import javax.persistence.EntityNotFoundException;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class SharedPersonImpl implements SharedPersonPort {

	private final PersonService personService;

	private final PersonMapper personMapper;

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
	public String parseCompletePersonName(String givenName, String familyNames, String selfDeterminateName) {
		log.debug("Input paremeters -> givenName {}, familyNames {}, selfDeterminationName {} ",
				givenName, familyNames, selfDeterminateName);
		return personService.parseCompletePersonName(givenName, familyNames, selfDeterminateName);
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

	@Override
	public Optional<PersonDto> getPersonData(Integer patientId) {
			Optional<PersonBo> personBo = personService.getPersonData(patientId);
			if (personBo.isEmpty()) {
				throw new EntityNotFoundException("No se encontró ningún dato para el paciente con ID: " + patientId);
			}
			return Optional.ofNullable(PersonDto.builder()
					.firstName(personBo.get().getFirstName())
					.middleNames(personBo.get().getMiddleNames())
					.lastName(personBo.get().getLastName())
					.otherLastNames(personBo.get().getOtherLastNames())
					.identificationTypeId(personBo.get().getIdentificationTypeId())
					.identificationTypeDescription(personBo.get().getIdentificationTypeDescription())
					.identificationNumber(personBo.get().getIdentificationNumber())
					.genderId(personBo.get().getGenderId())
					.genderDescription(personBo.get().getGenderDescription())
					.birthDate(personBo.get().getBirthDate())
					.cuil(personBo.get().getCuil())
					.selfDeterminationName(personBo.get().getSelfDeterminationName())
					.selfDeterminationGender(personBo.get().getSelfDeterminationGender())
					.selfDeterminationGenderDescription(personBo.get().getSelfDeterminationGenderDescription())
					.build());
	}

	@Override
	public String parseFormalPersonName(String firstName, String middleNames, String lastName, String otherLastNames, String selfDeterminateName) {
		return personService.parseFormalPersonName(firstName,middleNames,lastName,otherLastNames,selfDeterminateName);
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
