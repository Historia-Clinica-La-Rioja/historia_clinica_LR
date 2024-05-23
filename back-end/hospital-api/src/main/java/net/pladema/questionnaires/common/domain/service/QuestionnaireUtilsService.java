package net.pladema.questionnaires.common.domain.service;

import net.pladema.person.repository.PersonRepository;
import net.pladema.person.repository.entity.Person;

import net.pladema.staff.repository.HealthcareProfessionalRepository;
import net.pladema.staff.repository.entity.HealthcareProfessional;
import net.pladema.user.repository.UserPersonRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class QuestionnaireUtilsService {

	@Autowired
	private final UserPersonRepository userPersonRepository;

	@Autowired
	private final PersonRepository personRepository;

	@Autowired
	private final HealthcareProfessionalRepository healthcareProfessionalRepository;

	public QuestionnaireUtilsService(UserPersonRepository userPersonRepository, PersonRepository personRepository, HealthcareProfessionalRepository healthcareProfessionalRepository) {
		this.userPersonRepository = userPersonRepository;
		this.personRepository = personRepository;
		this.healthcareProfessionalRepository = healthcareProfessionalRepository;
	}

	public String fullNameFromPerson(Person person) {

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

	public Person getPersonByUserId(Integer userId) {

		Integer personId = userPersonRepository.getPersonIdByUserId(userId)
				.orElseThrow(() -> new RuntimeException("Person id not found for user id " + userId));

		return personRepository.findById(personId)
				.orElseThrow(() -> new RuntimeException("Person not found for id " + personId));
	}

	public HealthcareProfessional getHealthcareProfessionalByUserId(Integer userId) {

		Integer personId = userPersonRepository.getPersonIdByUserId(userId)
				.orElseThrow(() -> new RuntimeException("Person id not found for user id " + userId));

		return healthcareProfessionalRepository.findByPersonId(personId)
				.orElseThrow(() -> new RuntimeException("HealthcareProfessional not found for person id " + personId));
	}

}
