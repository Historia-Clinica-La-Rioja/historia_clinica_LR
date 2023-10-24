package net.pladema.questionnaires.frail.getsummary.controller;

import java.io.IOException;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import io.swagger.v3.oas.annotations.tags.Tag;
import net.pladema.questionnaires.common.dto.QuestionnaireSummary;

@Tag(name = "Patient consultation - Frail", description = "Patient consultation summary - Frail")
public interface GetFrailSummary {

	@GetMapping("/frail/{questionnaireId}")
	ResponseEntity<QuestionnaireSummary> getPatientConsultationFrail(@PathVariable(name = "institutionId") Integer institutionId, @PathVariable(name = "patientId") Integer patientId, @PathVariable(name = "questionnaireId") Integer questionnaire) throws IOException;
}
