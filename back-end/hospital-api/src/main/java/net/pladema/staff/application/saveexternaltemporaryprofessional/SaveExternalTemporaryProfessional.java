package net.pladema.staff.application.saveexternaltemporaryprofessional;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import net.pladema.person.repository.entity.Person;
import net.pladema.person.service.PersonService;
import net.pladema.staff.domain.ExternalTemporaryHealthcareProfessionalBo;

import net.pladema.staff.service.HealthcareProfessionalService;

import net.pladema.staff.service.domain.HealthcareProfessionalCompleteBo;

import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class SaveExternalTemporaryProfessional {

	private final PersonService personService;

	private final HealthcareProfessionalService healthcareProfessionalService;

	public Integer run(ExternalTemporaryHealthcareProfessionalBo temporaryProfessional){
		log.debug("Input parameters -> temporaryProfessional {}", temporaryProfessional);
		Integer personId = savePerson(temporaryProfessional);
		Integer professionalId = healthcareProfessionalService.saveProfessional(new HealthcareProfessionalCompleteBo(null, personId));
		log.debug("Output result -> professionalId {}", professionalId);
		return professionalId;
	}

	private Integer savePerson(ExternalTemporaryHealthcareProfessionalBo temporaryProfessional){
		Person person = new Person(temporaryProfessional.getFirstName(), temporaryProfessional.getLastName(), temporaryProfessional.getIdentificationTypeId(), temporaryProfessional.getIdentificationNumber());
		person = personService.addPerson(person);
		return person.getId();
	}
}
