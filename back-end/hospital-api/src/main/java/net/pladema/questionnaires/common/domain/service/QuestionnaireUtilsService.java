package net.pladema.questionnaires.common.domain.service;

import net.pladema.person.repository.entity.Person;

import org.springframework.stereotype.Service;

@Service
public class QuestionnaireUtilsService {

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

}
