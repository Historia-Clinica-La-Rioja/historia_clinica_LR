package net.pladema.questionnaires.general.getall.controller;

import java.util.List;

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
import net.pladema.questionnaires.common.domain.QuestionnaireResponseII;
import net.pladema.questionnaires.general.getall.domain.service.GetAllService;


@RestController
@RequestMapping
@Tag(name = "Patient questionnaires and assessments", description = "Patient questionnaires and assessments")
public class GetAllController {

	private final Logger logger = LoggerFactory.getLogger(GetAllController.class);

	@Autowired
	private final GetAllService getAllService;

    public GetAllController(GetAllService getAllService) {
        this.getAllService = getAllService;
    }

	@PreAuthorize("hasPermission(#institutionId, 'ESPECIALISTA_MEDICO, PROFESIONAL_DE_SALUD, ENFERMERO_ADULTO_MAYOR, ENFERMERO, ESPECIALISTA_EN_ODONTOLOGIA')")
	@GetMapping("institution/{institutionId}/patient/{patientId}/all-questionnaire-responses")
	public ResponseEntity<List<QuestionnaireResponseII>> getResponsesByPatientIdWithDetails (
			@PathVariable Integer patientId,
			@PathVariable Integer institutionId
	) {
		try {
			List<QuestionnaireResponseII> responses = getAllService.getResponsesByPatientIdWithDetails(patientId);

			for (QuestionnaireResponseII response : responses) {

				Integer createdByHealthcareProfessionalId = response.getCreatedBy();
				Integer updatedByHealthcareProfessionalId = response.getUpdatedBy();

				String createdByHealthcareProfessionalFullName = getAllService.getFullNameByHealthcareProfessionalId(createdByHealthcareProfessionalId);
				String updatedByHealthcareProfessionalFullName = getAllService.getFullNameByHealthcareProfessionalId(updatedByHealthcareProfessionalId);

				response.setCreatedByFullName(createdByHealthcareProfessionalFullName);
				response.setUpdatedByFullName(updatedByHealthcareProfessionalFullName);
			}
			return new ResponseEntity<>(responses, HttpStatus.OK);
		} catch (Exception e) {
			logger.error("Error retrieving questionnaire responses", e);
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

}
