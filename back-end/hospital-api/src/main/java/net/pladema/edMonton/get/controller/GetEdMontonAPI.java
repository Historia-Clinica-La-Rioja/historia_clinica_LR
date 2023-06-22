package net.pladema.edMonton.get.controller;

import io.swagger.v3.oas.annotations.tags.Tag;

import net.pladema.edMonton.getPdfEdMonton.dto.QuestionnaireDto;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.io.IOException;

@Tag(name = "Patient Consultation - EdMonton", description = "Patient Consultation - EdMonton")
public interface GetEdMontonAPI {

	@GetMapping("/edMonton")
	QuestionnaireDto getPatientConsultationEdMontonTest(
			@PathVariable(name = "institutionId") Integer institutionId,
			@PathVariable(name = "patientId") Integer patientId ) throws IOException;
}
