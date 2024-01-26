package net.pladema.questionnaires.general.getall.domain.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.webjars.NotFoundException;

import net.pladema.person.repository.PersonRepository;
import net.pladema.person.repository.entity.Person;
import net.pladema.questionnaires.common.repository.entity.QuestionnaireResponse;
import net.pladema.questionnaires.common.repository.QuestionnaireResponseRepository;
import net.pladema.staff.repository.HealthcareProfessionalRepository;
import net.pladema.staff.repository.entity.HealthcareProfessional;

@Service
public class GetAllService {

	@Autowired
	private final QuestionnaireResponseRepository questionnaireResponseRepository;

	@Autowired
	private final HealthcareProfessionalRepository healthcareProfessionalRepository;

	@Autowired
	private final PersonRepository personRepository;

    public GetAllService(QuestionnaireResponseRepository questionnaireResponseRepository, HealthcareProfessionalRepository healthcareProfessionalRepository, PersonRepository personRepository) {
        this.questionnaireResponseRepository = questionnaireResponseRepository;
        this.healthcareProfessionalRepository = healthcareProfessionalRepository;
        this.personRepository = personRepository;
    }

	public List<QuestionnaireResponse> getResponsesByPatientIdWithDetails(Integer patientId) {
		Sort sort = Sort.by(Sort.Order.desc("id"));

		return questionnaireResponseRepository.findResponsesWithCreatedByDetails(patientId, sort);
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
