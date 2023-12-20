package net.pladema.questionnaires.general.getall.domain.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.webjars.NotFoundException;

import net.pladema.person.repository.PersonRepository;
import net.pladema.person.repository.entity.Person;
import net.pladema.questionnaires.general.getall.domain.QuestionnaireResponseII;
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

//	public List<QuestionnaireResponseDto> convertResponsesToDto(List<QuestionnaireResponseII> responses) {
//		List<QuestionnaireResponseDto> dtos = new ArrayList<>();
//
//		for (QuestionnaireResponseII response : responses) {
//			QuestionnaireResponseDto dto = new QuestionnaireResponseDto();
//
//			dto.setId(response.getId());
//			dto.setStatusId(response.getStatusId());
//			dto.setCreatedByFullName(response.getCreatedByFullName());
//			dto.setCreatedByLicenseNumber(response.getCreatedByLicenseNumber());
//			dto.setCreatedOn(response.getCreatedOn());
//			dto.setQuestionnaireType(response.getQuestionnaireType());
//
//			if (response.getCreatedOn().isEqual(response.getUpdatedOn())) {
//
//			} else {
//				dto.setUpdatedByFullName(response.getUpdatedByFullName());
//				dto.setUpdatedByLicenseNumber(response.getUpdatedByLicenseNumber());
//				dto.setUpdatedOn(response.getUpdatedOn());
//			}
//
//			dtos.add(dto);
//
//		}
//
//		return dtos;
//
//	}

}
