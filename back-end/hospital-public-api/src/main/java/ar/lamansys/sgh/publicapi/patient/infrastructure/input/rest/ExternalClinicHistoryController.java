package ar.lamansys.sgh.publicapi.patient.infrastructure.input.rest;

import ar.lamansys.sgh.publicapi.patient.application.saveexternalclinicalhistory.SaveExternalClinicalHistory;
import ar.lamansys.sgh.publicapi.patient.domain.ExternalClinicalHistoryBo;
import ar.lamansys.sgh.shared.infrastructure.input.service.ExternalClinicalHistoryDto;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/public-api/external-clinical-history")
@Tag(name = "PublicApi Pacientes", description = "External Clinical History Api")
public class ExternalClinicHistoryController {

	private final SaveExternalClinicalHistory saveExternalClinicalHistory;

	@PostMapping()
	@ResponseStatus(code = HttpStatus.CREATED)
	public Integer save(@RequestBody ExternalClinicalHistoryDto externalClinicalHistoryDto) {
		log.debug("Input parameter -> externalClinicalHistoryDto {} ", externalClinicalHistoryDto);
		Integer result = saveExternalClinicalHistory.run(mapToBo(externalClinicalHistoryDto));
		log.debug("Output parameter -> result {} ", result);
		return result;
	}

	private ExternalClinicalHistoryBo mapToBo(ExternalClinicalHistoryDto externalClinicalHistoryDto) {
		return	ExternalClinicalHistoryBo.builder()
				.patientGender(externalClinicalHistoryDto.getPatientGender())
				.patientDocumentType(externalClinicalHistoryDto.getPatientDocumentType())
				.patientDocumentNumber(externalClinicalHistoryDto.getPatientDocumentNumber())
				.notes(externalClinicalHistoryDto.getNotes())
				.consultationDate(externalClinicalHistoryDto.getConsultationDate())
				.institution(externalClinicalHistoryDto.getInstitution())
				.professionalName(externalClinicalHistoryDto.getProfessionalName())
				.professionalSpecialty(externalClinicalHistoryDto.getProfessionalSpecialty())
				.build();
	}
}
