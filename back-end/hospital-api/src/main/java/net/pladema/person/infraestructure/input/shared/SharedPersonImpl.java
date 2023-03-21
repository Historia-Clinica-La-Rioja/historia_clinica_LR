package net.pladema.person.infraestructure.input.shared;

import ar.lamansys.sgh.shared.infrastructure.input.service.PersonInfoDto;
import ar.lamansys.sgh.shared.infrastructure.input.service.SharedPersonPort;
import lombok.RequiredArgsConstructor;
import net.pladema.person.repository.entity.Gender;
import net.pladema.person.service.PersonService;

import net.pladema.person.service.domain.PersonInformationBo;

import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SharedPersonImpl implements SharedPersonPort {

	private final PersonService personService;

	@Override
	public PersonInfoDto getPersonByIdentificationInfo(String identificationType, String identificationNumber) {
		var result = personService.getPersonInformationByIdentificationData(identificationType, identificationNumber, Gender.FEMALE);
		return mapTo(result);
	}

	@Override
	public String getCountryIsoCodeFromPerson(Integer personId) {
		return personService.getCountryIsoCodeFromPerson(personId);
	}

	private PersonInfoDto mapTo(PersonInformationBo personInformationBo) {
		return PersonInfoDto.builder()
				.firstName(personInformationBo.getFirstName())
				.middleNames(personInformationBo.getMiddleNames())
				.lastName(personInformationBo.getLastName())
				.birthDate(personInformationBo.getBirthDate())
				.identificationNumber(personInformationBo.getIdentificationNumber())
				.identificationTypeDescription(personInformationBo.getIdentificationTypeDescription())
				.build();
	}

}
