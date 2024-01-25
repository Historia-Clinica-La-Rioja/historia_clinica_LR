package net.pladema.questionnaires.general.getanswers.controller;


import io.swagger.v3.oas.annotations.tags.Tag;

import net.pladema.questionnaires.common.dto.AnswerDTO;
import net.pladema.questionnaires.general.getanswers.domain.service.GetAnswersService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping
@Tag(name = "Patient questionnaires and assessments", description = "Patient questionnaires and assessments")
public class GetAnswersController {

	@Autowired
	private GetAnswersService answersService;

	@PreAuthorize("hasPermission(#institutionId, 'ESPECIALISTA_MEDICO, PROFESIONAL_DE_SALUD, ENFERMERO_ADULTO_MAYOR, ENFERMERO, ESPECIALISTA_EN_ODONTOLOGIA')")
	@GetMapping("institution/{institutionId}/questionnaire/{questionnaireResponseId}/answers")
	public ResponseEntity<List<AnswerDTO>> getAnswersForResponse (
		@PathVariable Integer institutionId, @PathVariable Integer questionnaireResponseId
	) {
		List<AnswerDTO> answers = answersService.getAnswersForResponse(questionnaireResponseId);

		if (answers.isEmpty()) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}

		return new ResponseEntity<>(answers, HttpStatus.OK);
	}

}
