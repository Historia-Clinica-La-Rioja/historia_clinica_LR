package net.pladema.edMonton.get.controller;

import io.swagger.v3.oas.annotations.tags.Tag;

import net.pladema.edMonton.get.controller.dto.EdMontonAnswers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.io.IOException;
import java.util.List;

@Tag(name = "Patient Consultation - EdMonton", description = "Patient Consultation - EdMonton")
public interface GetEdMontonAPI {

	@GetMapping("/edMonton")
	ResponseEntity<List<EdMontonAnswers>> getPatientConsultationEdMontonTest(
			@PathVariable(name = "institutionId") Integer institutionId,
			@PathVariable(name = "patientId") Integer patientId ) throws IOException;
}
