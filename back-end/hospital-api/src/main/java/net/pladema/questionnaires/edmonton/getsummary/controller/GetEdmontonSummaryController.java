package net.pladema.questionnaires.edmonton.getsummary.controller;

import net.pladema.questionnaires.edmonton.get.domain.service.GetEdmontonService;

import net.pladema.questionnaires.common.dto.QuestionnaireSummary;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@Validated
@RequestMapping("/institution/{institutionId}/patient/{patientId}/hce/general-state/summary")
public class GetEdmontonSummaryController implements GetEdmontonSummary {

	private final GetEdmontonService getQuestionnaireService;

	public GetEdmontonSummaryController(GetEdmontonService getQuestionnaireService) {
		this.getQuestionnaireService = getQuestionnaireService;
	}

	@Override
	public ResponseEntity<QuestionnaireSummary> getPatientConsultationEdmonton(Integer institutionId, Integer patientId, Integer questionnaire) {

		QuestionnaireSummary questionnaireSummary = new QuestionnaireSummary();

		Optional<QuestionnaireSummary> result = getQuestionnaireService.findQuestionnaireSummary(questionnaire);

		questionnaireSummary.setTitle("Escala de fragilidad 'Edmonton'");
		questionnaireSummary.setProfessionalName(result.get().getProfessionalName());
		questionnaireSummary.setProfessionalMiddleName(result.get().getProfessionalMiddleName());
		questionnaireSummary.setProfessionalLastName(result.get().getProfessionalLastName());
		questionnaireSummary.setCreatedOn(result.get().getCreatedOn());
		questionnaireSummary.setLicenseNumber(result.get().getLicenseNumber());

		return ResponseEntity.ok(questionnaireSummary);
	}
}
