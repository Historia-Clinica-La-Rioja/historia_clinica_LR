package net.pladema.questionnaires.general.create.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.tags.Tag;
import net.pladema.questionnaires.common.repository.entity.QuestionnaireResponse;
import net.pladema.questionnaires.common.dto.QuestionnaireResponseDTO;
import net.pladema.questionnaires.general.create.domain.service.CreateQuestionnaireService;

@RestController
@RequestMapping
@Tag(name = "Patient questionnaires and assessments", description = "Patient questionnaires and assessments")
public class CreateQuestionnaireController {

	@Autowired
	private CreateQuestionnaireService questionnaireService;

	@PreAuthorize("hasPermission(#institutionId, 'ESPECIALISTA_MEDICO, PROFESIONAL_DE_SALUD, ENFERMERO_ADULTO_MAYOR, ENFERMERO, ESPECIALISTA_EN_ODONTOLOGIA')")
	@PostMapping("institution/{institutionId}/patient/{patientId}/questionnaire/create")
	public ResponseEntity<QuestionnaireResponse> createQuestionnaireResponse(
			@RequestBody QuestionnaireResponseDTO responseDTO, @PathVariable Integer institutionId, @PathVariable Integer patientId
			) {
		QuestionnaireResponse createdResponse = questionnaireService.createQuestionnaireResponse(responseDTO, patientId);
		return new ResponseEntity<>(createdResponse, HttpStatus.CREATED);
	}
}
