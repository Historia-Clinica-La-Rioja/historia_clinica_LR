package net.pladema.questionnaires.general.getpdf.domain.service;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.webjars.NotFoundException;

import com.lowagie.text.DocumentException;

import net.pladema.establishment.repository.InstitutionRepository;
import net.pladema.establishment.repository.entity.Institution;
import net.pladema.person.repository.IdentificationTypeRepository;
import net.pladema.person.repository.PersonRepository;
import net.pladema.person.repository.entity.IdentificationType;
import net.pladema.person.repository.entity.Person;
import net.pladema.questionnaires.common.repository.entity.QuestionnaireResponse;
import net.pladema.questionnaires.common.repository.QuestionnaireResponseRepository;
import net.pladema.questionnaires.common.repository.entity.Answer;
import net.pladema.questionnaires.common.repository.AnswerRepository;
import net.pladema.staff.repository.HealthcareProfessionalRepository;
import net.pladema.staff.repository.entity.HealthcareProfessional;

@Service
public class GetQuestionnairePdfService {

	@Autowired
	private final AnswerRepository answerRepository;

	@Autowired
	private final QuestionnaireResponseRepository questionnaireResponseRepository;

	@Autowired
	private final HealthcareProfessionalRepository healthcareProfessionalRepository;

    @Autowired
	private final InstitutionRepository institutionRepository;

	@Autowired
	private final PersonRepository personRepository;

	@Autowired
	private final IdentificationTypeRepository identificationTypeRepository;

    public GetQuestionnairePdfService(AnswerRepository answerRepository, QuestionnaireResponseRepository questionnaireResponseRepository, HealthcareProfessionalRepository healthcareProfessionalRepository, InstitutionRepository institutionRepository, PersonRepository personRepository, IdentificationTypeRepository identificationTypeRepository) {
        this.answerRepository = answerRepository;
        this.questionnaireResponseRepository = questionnaireResponseRepository;
        this.healthcareProfessionalRepository = healthcareProfessionalRepository;
        this.institutionRepository = institutionRepository;
        this.personRepository = personRepository;
        this.identificationTypeRepository = identificationTypeRepository;
    }

    public Map<String, Object> createQuestionnaireContext(Integer questionnaireResponseId, Integer institutionId) throws DocumentException, IOException {
		QuestionnaireResponse response = questionnaireResponseRepository.findById(questionnaireResponseId)
				.orElseThrow(() -> new NotFoundException("Questionnaire response not found"));

		List<Answer> answers = answerRepository.findByQuestionnaireResponseId(questionnaireResponseId);

		HealthcareProfessional professional = healthcareProfessionalRepository.findById(response.getCreatedBy())
				.orElseThrow(() -> new NotFoundException("HealthcareProfessional not found"));

		Integer professionalPersonId = professional.getPersonId();

		Person professionalPerson = personRepository.findById(professionalPersonId)
				.orElseThrow(() -> new NotFoundException("Healthcare professional person not found"));

		Institution institution = institutionRepository.findById(institutionId)
				.orElseThrow(() -> new NotFoundException("Institution not found"));

		Person patientPerson = personRepository.findPersonByPatientId(response.getPatientId())
				.orElseThrow(() -> new NotFoundException("Patient person not found"));

		IdentificationType patientIdType = identificationTypeRepository.findById(patientPerson.getIdentificationTypeId())
				.orElseThrow(() -> new NotFoundException("Patient person not found"));

		String professionalPersonFullName = fullNameFromPerson(professionalPerson);
		String patientPersonFullName = fullNameFromPerson(patientPerson);

		Period patientAgePeriod = calculateAgeAtQuestionnaireResponseCreation(response);
		String patientAge = formatAge(patientAgePeriod);

		String formattedCreatedOn = formatDbTimestamp(response.getCreatedOn());

		Map<String, Object> context = new HashMap<>();
		context.put("response", response);
		context.put("answers", answers);
		context.put("professional", professional);
		context.put("professionalPerson", professionalPerson);
		context.put("professionalPersonFullName", professionalPersonFullName);
		context.put("institution", institution);
		context.put("patientPerson", patientPerson);
		context.put("patientPersonFullName", patientPersonFullName);
		context.put("patientAge", patientAge);
		context.put("patientIdType", patientIdType);
		context.put("formattedCreatedOn", formattedCreatedOn);

		return context;
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

	public Period calculateAgeAtQuestionnaireResponseCreation(QuestionnaireResponse questionnaireResponse) {

		Integer patientId = questionnaireResponse.getPatientId();
		Person person = personRepository.findById(patientId)
				.orElseThrow(() -> new NotFoundException("Person not found"));

		if (person != null && person.getBirthDate() != null) {
			LocalDateTime createdOn = questionnaireResponse.getCreatedOn();
			LocalDate birthDate = person.getBirthDate();

			return Period.between(birthDate, createdOn.toLocalDate());
		}

		return Period.ZERO;
	}

	public String formatAge(Period agePeriod) {
		int years = agePeriod.getYears();

		StringBuilder formattedAge = new StringBuilder();

		if (years > 0) {
			formattedAge.append(years).append(" ").append(years == 1 ? "año" : "años");
		}
		if (formattedAge.length() == 0) {
			formattedAge.append("Menos de un año");
		}

		return formattedAge.toString();
	}

	public String createQuestionnaireFileName(QuestionnaireResponse response) {
		Person person = personRepository.findPersonByPatientId(response.getPatientId())
				.orElseThrow(() -> new NotFoundException("Person not found"));

		String personName = fullNameFromPerson(person);

		String formattedResponseDate = formatDbTimestamp(response.getCreatedOn());

        return String.format("%s - %s - %s", personName, formattedResponseDate, response.getQuestionnaireType());
	}

	private static String formatDbTimestamp(LocalDateTime date) {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
		return date.format(formatter);
	}

}
