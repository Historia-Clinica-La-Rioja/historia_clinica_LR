package net.pladema.edMonton.get.controller;

import net.pladema.edMonton.get.controller.dto.EdMontonAnswers;

import net.pladema.edMonton.get.service.GetEdMontonService;
import net.pladema.edMonton.getPdfEdMonton.dto.QuestionnaireDto;
import net.pladema.edMonton.repository.domain.Answer;

import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@RestController
@Validated
@RequestMapping("/institution/{institutionId}/patient/{patientId}/hce/general-state")
public class GetEdMontonController implements GetEdMontonAPI {

	private GetEdMontonService getEdMontonService;

	public GetEdMontonController(GetEdMontonService getEdMontonService){this.getEdMontonService = getEdMontonService;}


	public QuestionnaireDto getPatientConsultationEdMontonTest
			(Integer institutionId,
			 Integer patientId) throws IOException {

		QuestionnaireDto questionnaireDto = new QuestionnaireDto();

		List<Answer> lst = getEdMontonService.findPatientEdMonton(patientId);
		for(Answer answer : lst){
			Integer id;
			id = answer.getQuestionnaireResponseId();
			questionnaireDto.setIdQuestionnaire(id);
		}
		questionnaireDto.setAnswers(createEdMontonDto(lst));

		return questionnaireDto;
	}

	private List<EdMontonAnswers> createEdMontonDto(List<Answer> lst){
		List<EdMontonAnswers> lstEdMonton = new ArrayList<>();
		EdMontonAnswers regDto;
		for(Answer qr : lst){
			regDto = new EdMontonAnswers();
			regDto.setIdQuestion(qr.getItemId());
			regDto.setIdAnswer(qr.getAnswerId());
			lstEdMonton.add(regDto);
		}
		return lstEdMonton;
	}
}
