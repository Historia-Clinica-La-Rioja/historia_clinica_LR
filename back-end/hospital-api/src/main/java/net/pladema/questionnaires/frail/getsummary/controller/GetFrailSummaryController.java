package net.pladema.questionnaires.frail.getsummary.controller;

import java.util.Optional;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import net.pladema.questionnaires.common.domain.service.GetQuestionnaireService;
import net.pladema.questionnaires.common.dto.QuestionnaireSummary;

@RestController
@Validated
@RequestMapping("/institution/{institutionId}/patient/{patientId}/hce/general-state/summary")
public class GetFrailSummaryController implements GetFrailSummary {

	private final GetQuestionnaireService getQuestionnaireService;

	public GetFrailSummaryController(GetQuestionnaireService getQuestionnaireService) {
		this.getQuestionnaireService = getQuestionnaireService;
	}

	@Override
	public ResponseEntity<QuestionnaireSummary> getPatientConsultationFrail(Integer institutionId, Integer patientId, Integer questionnaire) {

		QuestionnaireSummary questionnaireSummary = new QuestionnaireSummary();

		Optional<QuestionnaireSummary> result = getQuestionnaireService.findQuestionnaireSummary(questionnaire);

		questionnaireSummary.setTitle("Escala de fragilidad 'Frail'");
		questionnaireSummary.setProfessionalName(result.get().getProfessionalName());
		questionnaireSummary.setProfessionalMiddleName(result.get().getProfessionalMiddleName());
		questionnaireSummary.setProfessionalLastName(result.get().getProfessionalLastName());
		questionnaireSummary.setCreatedOn(result.get().getCreatedOn());
		questionnaireSummary.setLicenseNumber(result.get().getLicenseNumber());

		return ResponseEntity.ok(questionnaireSummary);
	}
}
