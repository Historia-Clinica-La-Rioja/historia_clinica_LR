package net.pladema.questionnaires.frail.get.controller;

import java.io.IOException;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import io.swagger.v3.oas.annotations.tags.Tag;
import net.pladema.questionnaires.common.dto.QuestionnaireDTO;

@Tag(name = "Patient consultation - Frail", description = "Patient consultation - Frail")
public interface GetFrailAPI {

	@GetMapping("/frail")
	QuestionnaireDTO getPatientConsultationFrailTest(
			@PathVariable(name = "institutionId") Integer institutionId,
			@PathVariable(name = "patientId") Integer patientId
	) throws IOException;
}
