package net.pladema.questionnaires.general.getall.domain.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.webjars.NotFoundException;

import net.pladema.person.repository.PersonRepository;
import net.pladema.person.repository.entity.Person;
import net.pladema.questionnaires.common.domain.QuestionnaireResponseII;
import net.pladema.questionnaires.general.getall.repository.GetAllRepository;
import net.pladema.staff.repository.HealthcareProfessionalRepository;
import net.pladema.staff.repository.entity.HealthcareProfessional;

@Service
public class GetAllService {

	@Autowired
	private final GetAllRepository getAllRepository;

	@Autowired
	private final HealthcareProfessionalRepository healthcareProfessionalRepository;

	@Autowired
	private final PersonRepository personRepository;

    public GetAllService(GetAllRepository getAllRepository, HealthcareProfessionalRepository healthcareProfessionalRepository, PersonRepository personRepository) {
        this.getAllRepository = getAllRepository;
        this.healthcareProfessionalRepository = healthcareProfessionalRepository;
        this.personRepository = personRepository;
    }

	public List<QuestionnaireResponseII> getResponsesByPatientIdWithDetails(Integer patientId) {
		return getAllRepository.findResponsesWithCreatedByDetails(patientId);
	}

	public String getFullNameByHealthcareProfessionalId(Integer healthcareProfessionalId) {

		HealthcareProfessional healthcareProfessional = healthcareProfessionalRepository.findById(healthcareProfessionalId)
				.orElseThrow(() -> new NotFoundException("HealthcareProfessional not found"));

		Integer personId = healthcareProfessional.getPersonId();

		Person person = personRepository.findById(personId)
				.orElseThrow(() -> new NotFoundException("Person not found"));

		StringBuilder fullNameBuilder = new StringBuilder();

		if (person.getLastName() != null) {
			fullNameBuilder.append(person.getLastName()).append(" ");
		}

		if (person.getOtherLastNames() != null) {
			fullNameBuilder.append(person.getOtherLastNames()).append(" ");
		}

		if (person.getFirstName() != null) {
			fullNameBuilder.append(person.getFirstName()).append(" ");
		}

		if (person.getMiddleNames() != null) {
			fullNameBuilder.append(person.getMiddleNames()).append(" ");
		}

		return fullNameBuilder.toString().trim();
	}

}
