package net.pladema.questionnaires.familybg.create.controller;

import java.io.IOException;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import io.swagger.v3.oas.annotations.tags.Tag;
import net.pladema.questionnaires.common.dto.CreateQuestionnaireDTO;

@Tag(name = "Patient consultation - Family background", description = "Patient consultation - Family background")
public interface CreateFamilyBgAPI {
	@PostMapping("/familybg")
	ResponseEntity<Boolean> createPatientFamilyBg(@PathVariable(name = "institutionId") Integer institutionId, @PathVariable(name = "patientId") Integer patientId, @RequestBody CreateQuestionnaireDTO createFamilyBgDto) throws IOException;
}
