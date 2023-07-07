package net.pladema.edMonton.create.controller;

import io.swagger.v3.oas.annotations.tags.Tag;

import net.pladema.edMonton.create.controller.dto.CreateEdMontonDto;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.io.IOException;

@Tag(name = "Patient Consultation - EdMonton", description = "Patient Consultation - EdMonton")
public interface CreateEdMontonAPI {

	@PostMapping("/edmonton")
	ResponseEntity<Boolean> createPatientEdMonton(
			@PathVariable(name = "institutionId") Integer institutionId,
			@PathVariable(name = "patientId") Integer patientId,
			@RequestBody CreateEdMontonDto createEdMontonDto) throws IOException;
}
