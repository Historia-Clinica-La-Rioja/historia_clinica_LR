package net.pladema.questionnaires.familybg.getsummary.controller;

import java.util.Optional;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import net.pladema.questionnaires.common.dto.QuestionnaireSummary;
import net.pladema.questionnaires.familybg.get.domain.service.GetFamilyBgService;

@RestController
@Validated
@RequestMapping("/institution/{institutionId}/patient/{patientId}/hce/general-state/summary")
public class GetFamilyBgSummaryController implements GetFamilyBgSummaryAPI {

	private final GetFamilyBgService getQuestionnaireService;

	public GetFamilyBgSummaryController(GetFamilyBgService getQuestionnaireService) {
		this.getQuestionnaireService = getQuestionnaireService;
	}

	@Override
	public ResponseEntity<QuestionnaireSummary> getPatientConsultationFamilyBg(Integer institutionId, Integer patientId, Integer questionnaire) {
		QuestionnaireSummary questionnaireSummary = new QuestionnaireSummary();

		Optional<QuestionnaireSummary> result = getQuestionnaireService.findQuestionnaireSummary(questionnaire);

		questionnaireSummary.setTitle("Cuestionario de antecedentes familiares");
		questionnaireSummary.setProfessionalName(result.get().getProfessionalName());
		questionnaireSummary.setProfessionalMiddleName(result.get().getProfessionalMiddleName());
		questionnaireSummary.setProfessionalLastName(result.get().getProfessionalLastName());
		questionnaireSummary.setCreatedOn(result.get().getCreatedOn());
		questionnaireSummary.setLicenseNumber(result.get().getLicenseNumber());

		return ResponseEntity.ok(questionnaireSummary);
	}
}
