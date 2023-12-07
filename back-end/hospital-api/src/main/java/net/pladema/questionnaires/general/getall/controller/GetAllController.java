package net.pladema.questionnaires.general.getall.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.tags.Tag;
import net.pladema.questionnaires.general.getall.domain.QuestionnaireResponseII;
import net.pladema.questionnaires.general.getall.domain.service.GetAllService;


@RestController
@RequestMapping
@Tag(name = "Patient consultation - General", description = "Patient consultation - General")
public class GetAllController {

	@Autowired
	private GetAllService getAllService;

	@GetMapping("/patient/{patientId}/questionnaire-responses")
	public ResponseEntity<List<QuestionnaireResponseII>> getResponsesByPatientId(
			@PathVariable(name = "patientId") Integer patientId
	) {
		try {
			List<QuestionnaireResponseII> responses = getAllService.getResponsesByPatientId(patientId);
			return new ResponseEntity<>(responses, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
}
