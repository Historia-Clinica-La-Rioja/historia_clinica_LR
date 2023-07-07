package net.pladema.edMonton.get.controller;

import io.swagger.v3.oas.annotations.tags.Tag;

import net.pladema.edMonton.get.controller.dto.EdMontonSummary;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.io.IOException;

@Tag(name = "Patient Consultation - EdMonton", description = "Patient Consultation Summary - EdMonton")
public interface GetSummaryEdMontonTest {


	@GetMapping("/edMonton/{edMontonId}")
	ResponseEntity<EdMontonSummary> getPatientConsultationEdMontonTest(
			@PathVariable(name = "institutionId") Integer institutionId,
			@PathVariable(name = "patientId") Integer patientId,
			@PathVariable(name = "edMontonId") Integer edMonton) throws IOException;
}
