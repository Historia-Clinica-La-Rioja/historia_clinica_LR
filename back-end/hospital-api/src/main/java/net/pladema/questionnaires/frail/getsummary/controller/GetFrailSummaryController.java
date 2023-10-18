package net.pladema.questionnaires.frail.getsummary.controller;

import java.util.Optional;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import net.pladema.questionnaires.common.dto.QuestionnaireSummary;
import net.pladema.questionnaires.frail.get.domain.service.GetFrailService;

@RestController
@Validated
@RequestMapping("/institution/{institutionId}/patient/{patientId}/hce/general-state/summary")
public class GetFrailSummaryController implements GetFrailSummary {

	private final GetFrailService getQuestionnaireService;

	public GetFrailSummaryController(GetFrailService getFrailService) {
		this.getQuestionnaireService = getFrailService;
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
