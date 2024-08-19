package net.pladema.questionnaires.general.getall.domain.service;

import java.util.List;

import net.pladema.questionnaires.common.domain.service.QuestionnaireUtilsService;

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

	public GetAllService(QuestionnaireResponseRepository questionnaireResponseRepository, HealthcareProfessionalRepository healthcareProfessionalRepository, PersonRepository personRepository, QuestionnaireUtilsService utilsService) {
        this.questionnaireResponseRepository = questionnaireResponseRepository;
	}

	public List<QuestionnaireResponse> getResponsesByPatientIdWithDetails(Integer patientId) {
		Sort sort = Sort.by(Sort.Order.desc("id"));

		return questionnaireResponseRepository.findResponsesWithCreatedByDetails(patientId, sort);
	}

	public String getFinalQuestionnaireResult(Integer questionnaireResponseId) {
		return questionnaireResponseRepository.findLastOptionValueByQuestionnaireResponseId(questionnaireResponseId);
	}

}
