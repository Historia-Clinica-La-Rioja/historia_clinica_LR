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
import net.pladema.questionnaires.common.domain.QuestionnaireResponseII;
import net.pladema.questionnaires.general.create.domain.dto.QuestionnaireResponseDTO;
import net.pladema.questionnaires.general.create.domain.service.CreateQuestionnaireService;

@RestController
@RequestMapping
@Tag(name = "Patient consultation - General", description = "Patient consultation - General")
public class CreateQuestionnaireController {

	@Autowired
	private CreateQuestionnaireService questionnaireService;

	@PreAuthorize("hasPermission(#institutionId, 'ESPECIALISTA_MEDICO, PROFESIONAL_DE_SALUD, ENFERMERO_ADULTO_MAYOR, ENFERMERO, ESPECIALISTA_EN_ODONTOLOGIA')")
	@PostMapping("institution/{institutionId}/patient/{patientId}/questionnaire/create")
	public ResponseEntity<QuestionnaireResponseII> createQuestionnaireResponse(
			@RequestBody QuestionnaireResponseDTO responseDTO, @PathVariable Integer institutionId, @PathVariable Integer patientId
			) {
		QuestionnaireResponseII createdResponse = questionnaireService.createQuestionnaireResponse(responseDTO, patientId);
		return new ResponseEntity<>(createdResponse, HttpStatus.CREATED);
	}
}
