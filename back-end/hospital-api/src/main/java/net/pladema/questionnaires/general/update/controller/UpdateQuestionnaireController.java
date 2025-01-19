package net.pladema.questionnaires.general.update.controller;

import ar.lamansys.sgx.shared.exceptions.NotFoundException;
import io.swagger.v3.oas.annotations.tags.Tag;

import net.pladema.questionnaires.common.dto.UpdateResponseDTO;
import net.pladema.questionnaires.general.update.domain.service.UpdateQuestionnaireService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping
@Tag(name = "Patient questionnaires and assessments", description = "Patient questionnaires and assessments")
public class UpdateQuestionnaireController {

	@Autowired
	private UpdateQuestionnaireService service;

	@PreAuthorize("hasPermission(#institutionId, 'ESPECIALISTA_MEDICO, PROFESIONAL_DE_SALUD, ENFERMERO_ADULTO_MAYOR, ENFERMERO, ESPECIALISTA_EN_ODONTOLOGIA')")
	@PutMapping("institution/{institutionId}/questionnaire/{questionnaireResponseId}/update")
	public ResponseEntity<String> updateAnswers(
			@RequestBody UpdateResponseDTO responseDTO, @PathVariable Integer institutionId, @PathVariable Integer questionnaireResponseId
	) {
		try {
			service.updateAnswersAndQuestionnaireData(responseDTO, questionnaireResponseId);
			return ResponseEntity.ok("Updated questionnaire successfully");
		} catch (NotFoundException e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to update questionnaire: " + e.getMessage());
		}
	}

}