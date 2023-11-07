package net.pladema.questionnaires.familybg.get.controller;

import net.pladema.questionnaires.common.domain.Answer;
import net.pladema.questionnaires.common.dto.QuestionnaireAnswers;
import net.pladema.questionnaires.common.dto.QuestionnaireDTO;
import net.pladema.questionnaires.familybg.get.domain.service.GetFamilyBgService;

import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@RestController
@Validated
@RequestMapping("/institution/{institutionId}/patient/{patientId}/hce/general-state")
public class GetFamilyBgController implements GetFamilyBgAPI {
	private final GetFamilyBgService getQuestionnaireService;

	public GetFamilyBgController(GetFamilyBgService getQuestionnaireService) {
		this.getQuestionnaireService = getQuestionnaireService;
	}

	public QuestionnaireDTO getPatientConsultationFamilyBgTest(Integer institutionId, Integer patientId) throws IOException {

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
			questionnaireLst.add(regDTO);
		}
		return questionnaireLst;
	}
}
