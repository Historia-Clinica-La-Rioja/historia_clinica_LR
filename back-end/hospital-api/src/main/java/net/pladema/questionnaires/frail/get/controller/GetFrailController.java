package net.pladema.questionnaires.frail.get.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import net.pladema.questionnaires.common.domain.Answer;
import net.pladema.questionnaires.common.dto.QuestionnaireAnswers;
import net.pladema.questionnaires.common.dto.QuestionnaireDTO;
import net.pladema.questionnaires.frail.get.domain.service.GetFrailService;

@RestController
@Validated
@RequestMapping("/institution/{institutionId}/patient/{patientId}/hce/general-state")
public class GetFrailController implements GetFrailAPI {

	private final GetFrailService getQuestionnaireService;

	public GetFrailController(GetFrailService getQuestionnaireService) {
		this.getQuestionnaireService = getQuestionnaireService;
	}

	public QuestionnaireDTO getPatientConsultationFrailTest(Integer institutionId, Integer patientId) {

		QuestionnaireDTO questionnaireDTO = new QuestionnaireDTO();
		List<Answer> lst = getQuestionnaireService.findPatientQuestionnaire(patientId);
		for (Answer answer : lst) {
			Integer id = answer.getQuestionnaireResponseId();
			questionnaireDTO.setQuestionnaireId(id);
		}
		questionnaireDTO.setAnswers(createQuestionnaireDTO(lst));

		return questionnaireDTO;
	}

	private List<QuestionnaireAnswers> createQuestionnaireDTO(List<Answer> lst) {
		List<QuestionnaireAnswers> questionnaireLst = new ArrayList<>();
		QuestionnaireAnswers regDTO;
		for (Answer qr : lst) {
			regDTO = new QuestionnaireAnswers();
			regDTO.setQuestionId(qr.getItemId());
			regDTO.setAnswerId(qr.getAnswerId());
			regDTO.setValue(qr.getValue());
			questionnaireLst.add(regDTO);
		}
		return questionnaireLst;
	}
}
