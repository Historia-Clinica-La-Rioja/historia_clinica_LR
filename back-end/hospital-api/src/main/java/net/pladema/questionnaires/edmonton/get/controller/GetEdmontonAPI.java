package net.pladema.questionnaires.edmonton.get.controller;

import java.io.IOException;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import io.swagger.v3.oas.annotations.tags.Tag;
import net.pladema.questionnaires.common.dto.QuestionnaireDTO;

@Tag(name = "Patient consultation - Edmonton", description = "Patient consultation - Edmonton")
public interface GetEdmontonAPI {

	@GetMapping("/edmonton")
	QuestionnaireDTO getPatientConsultationEdmontonTest(
			@PathVariable(name = "institutionId") Integer institutionId,
			@PathVariable(name = "patientId") Integer patientId
	) throws IOException;
}
