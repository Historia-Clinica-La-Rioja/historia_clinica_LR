package ar.lamansys.sgh.publicapi.prescription.infrastructure.input.rest;

import ar.lamansys.sgh.publicapi.prescription.application.fetchMultipleCommercialPrescriptionsByIdAndDni.FetchMultipleCommercialPrescriptionsByIdAndIdentificationNumber;
import ar.lamansys.sgh.publicapi.prescription.domain.MultipleCommercialPrescriptionBo;
import ar.lamansys.sgh.publicapi.prescription.infrastructure.input.rest.dto.MultipleCommercialPrescriptionDto;
import ar.lamansys.sgh.publicapi.prescription.infrastructure.input.rest.mapper.PrescriptionMapper;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequiredArgsConstructor
@Tag(name = "PublicApi Recetas", description = "Public Api Digital Prescription Access")
@RequestMapping("/public-api/prescriptions/prescription/{prescriptionId}/identification/{identificationNumber}/get-multiple-commercial")
@RestController
public class FetchMultipleCommercialPrescriptionByIdController {

	private final FetchMultipleCommercialPrescriptionsByIdAndIdentificationNumber fetchMultipleCommercialPrescriptionsByIdAndIdentificationNumber;

	private final PrescriptionMapper prescriptionMapper;

	@GetMapping
	public @ResponseBody MultipleCommercialPrescriptionDto run(@PathVariable("prescriptionId") String prescriptionId, @PathVariable("identificationNumber") String identificationNumber) {
		log.debug("Input parameters -> prescriptionId {}, identificationNumber {}", prescriptionId, identificationNumber);
		MultipleCommercialPrescriptionBo resultBo = fetchMultipleCommercialPrescriptionsByIdAndIdentificationNumber.run(prescriptionId, identificationNumber);
		MultipleCommercialPrescriptionDto result = prescriptionMapper.toMultipleCommercialPrescriptionDto(resultBo);
		log.debug("Output -> {}", result);
		return result;
	}

}