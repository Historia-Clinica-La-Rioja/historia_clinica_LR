package ar.lamansys.sgh.publicapi.prescription.infrastructure.input.rest;

import ar.lamansys.sgh.publicapi.prescription.application.changeprescriptionstatemultiplecommercial.ChangePrescriptionStateMultipleCommercial;
import ar.lamansys.sgh.publicapi.prescription.domain.ChangePrescriptionStateMultipleBo;
import ar.lamansys.sgh.publicapi.prescription.infrastructure.input.rest.dto.ChangePrescriptionStateMultipleDto;
import ar.lamansys.sgh.publicapi.prescription.infrastructure.input.rest.mapper.PrescriptionMapper;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@Slf4j
@RequiredArgsConstructor
@Tag(name = "PublicApi Recetas", description = "Public Api Digital Prescription Access")
@RequestMapping("/public-api/prescriptions/prescription/{prescriptionId}/identification/{identificationNumber}/put-multiple-commercial")
@RestController
public class MultipleDispensePrescriptionController {

	private final PrescriptionMapper prescriptionMapper;

	private final ChangePrescriptionStateMultipleCommercial changePrescriptionStateMultipleCommercial;

	@PutMapping
	public ChangePrescriptionStateMultipleDto run(@PathVariable("prescriptionId") String pathPrescriptionId,
												  @PathVariable("identificationNumber") String identificationNumber,
												  @RequestBody @Valid ChangePrescriptionStateMultipleDto changePrescriptionStateMultipleDto) throws Exception {
		log.debug("Input parameters -> pathPrescriptionId {}, identificationNumber {}, changePrescriptionStateMultipleDto {}", pathPrescriptionId, identificationNumber, changePrescriptionStateMultipleDto);
		ChangePrescriptionStateMultipleBo changePrescriptionStateMedicationBo = prescriptionMapper.toChangePrescriptionStateMedicationBo(changePrescriptionStateMultipleDto, identificationNumber);
		changePrescriptionStateMultipleCommercial.run(changePrescriptionStateMedicationBo, pathPrescriptionId);
		log.debug("Process ended successfully");
		return changePrescriptionStateMultipleDto;
	}

}
