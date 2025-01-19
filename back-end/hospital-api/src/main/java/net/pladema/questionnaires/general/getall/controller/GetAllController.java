package net.pladema.questionnaires.general.getall.controller;

import java.util.List;

import net.pladema.person.repository.entity.Person;
import net.pladema.questionnaires.common.domain.service.QuestionnaireUtilsService;

import net.pladema.staff.repository.entity.HealthcareProfessional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.tags.Tag;
import net.pladema.questionnaires.common.repository.entity.QuestionnaireResponse;
import net.pladema.questionnaires.general.getall.domain.service.GetAllService;


@RestController
@RequestMapping
@Tag(name = "Patient questionnaires and assessments", description = "Patient questionnaires and assessments")
public class GetAllController {

	private final Logger logger = LoggerFactory.getLogger(GetAllController.class);

	@Autowired
	private final GetAllService getAllService;

	@Autowired
	private final QuestionnaireUtilsService utilsService;

    public GetAllController(GetAllService getAllService, QuestionnaireUtilsService utilsService) {
        this.getAllService = getAllService;
		this.utilsService = utilsService;
	}

	@PreAuthorize("hasPermission(#institutionId, 'ESPECIALISTA_MEDICO, PROFESIONAL_DE_SALUD, ENFERMERO_ADULTO_MAYOR, ENFERMERO, ESPECIALISTA_EN_ODONTOLOGIA')")
	@GetMapping("institution/{institutionId}/patient/{patientId}/all-questionnaire-responses")
	public ResponseEntity<List<QuestionnaireResponse>> getResponsesByPatientIdWithDetails (
			@PathVariable Integer patientId,
			@PathVariable Integer institutionId
	) {
		try {
			List<QuestionnaireResponse> responses = getAllService.getResponsesByPatientIdWithDetails(patientId);

			for (QuestionnaireResponse response : responses) {

				Person createdByPerson = utilsService.getPersonByUserId(response.getCreatedBy());
				Person updatedByPerson = utilsService.getPersonByUserId(response.getUpdatedBy());

				HealthcareProfessional createdByProfessional = utilsService.getHealthcareProfessionalByUserId(response.getCreatedBy());
				HealthcareProfessional updatedByProfessional = utilsService.getHealthcareProfessionalByUserId(response.getUpdatedBy());

				String createdByHealthcareProfessionalFullName = utilsService.fullNameFromPerson(createdByPerson);
				String updatedByHealthcareProfessionalFullName = utilsService.fullNameFromPerson(updatedByPerson);

				response.setCreatedByFullName(createdByHealthcareProfessionalFullName);
				response.setUpdatedByFullName(updatedByHealthcareProfessionalFullName);

				response.setCreatedByLicenseNumber(createdByProfessional.getLicenseNumber());
				response.setUpdatedByLicenseNumber(updatedByProfessional.getLicenseNumber());

				String finalResult = getAllService.getFinalQuestionnaireResult(response.getId());
				response.setQuestionnaireResult(finalResult);
			}
			return new ResponseEntity<>(responses, HttpStatus.OK);
		} catch (Exception e) {
			logger.error("Error retrieving questionnaire responses", e);
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

}
