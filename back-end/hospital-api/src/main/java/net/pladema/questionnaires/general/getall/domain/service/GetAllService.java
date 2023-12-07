package net.pladema.questionnaires.general.getall.domain.service;

import java.util.List;

import net.pladema.questionnaires.general.getall.domain.QuestionnaireII;
import net.pladema.questionnaires.general.getall.repository.QuestionnaireRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import net.pladema.questionnaires.general.getall.domain.QuestionnaireResponseII;
import net.pladema.questionnaires.general.getall.repository.GetAllRepository;

@Service
public class GetAllService {

	@Autowired
	private QuestionnaireRepository questionnaireRepository;

	@Autowired
	private GetAllRepository getAllRepository;

	public String getQuestionnaireNameById(Integer questionnaireId) {
		QuestionnaireII questionnaireII = questionnaireRepository.findById(questionnaireId).orElse(null);

        assert questionnaireII != null;
        return questionnaireII.getName();
	}

	public List<QuestionnaireResponseII> getResponsesByPatientId(Integer patientId) {
		return getAllRepository.findByPatientId(patientId);
	}
}
