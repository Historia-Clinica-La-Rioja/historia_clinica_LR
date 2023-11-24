package net.pladema.questionnaires.familybg.get.controller;

import java.io.IOException;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import io.swagger.v3.oas.annotations.tags.Tag;
import net.pladema.questionnaires.common.dto.QuestionnaireDTO;

@Tag(name = "Patient consultation - Family background", description = "Patient consultation - Family background")
public interface GetFamilyBgAPI {

	@GetMapping("/familybg")
	QuestionnaireDTO getPatientConsultationFamilyBgTest(
			@PathVariable(name = "institutionId") Integer institutionId,
			@PathVariable(name = "patientId") Integer patientId
	) throws IOException;
}
