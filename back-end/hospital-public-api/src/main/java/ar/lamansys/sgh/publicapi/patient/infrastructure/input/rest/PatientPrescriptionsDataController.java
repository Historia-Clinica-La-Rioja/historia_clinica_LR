package ar.lamansys.sgh.publicapi.patient.infrastructure.input.rest;

import ar.lamansys.sgh.publicapi.prescription.infrastructure.input.rest.dto.PrescriptionsDataDto;
import ar.lamansys.sgh.publicapi.prescription.domain.PrescriptionsDataBo;
import ar.lamansys.sgh.publicapi.prescription.infrastructure.input.rest.mapper.PrescriptionMapper;

import lombok.AllArgsConstructor;

import lombok.extern.slf4j.Slf4j;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import ar.lamansys.sgh.publicapi.patient.application.fetchprescriptionsdatabydni.FetchPrescriptionsDataByDni;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@AllArgsConstructor
@RestController
@RequestMapping("/public-api/patient/{identificationNumber}")
@Tag(name = "PublicApi Recetas de paciente", description = "Public Api Digital Prescription Access")
public class PatientPrescriptionsDataController {

	private static final String INPUT = "Input data -> ";
	private final FetchPrescriptionsDataByDni fetchPrescriptionsDataByDni;
	private final PrescriptionMapper prescriptionMapper;

	@GetMapping("/prescriptions")
	public List<PrescriptionsDataDto> getPrescriptions(
			@PathVariable("identificationNumber") String identificationNumber
	) {
		log.debug(INPUT + "identificationNumber {}", identificationNumber);
		List<PrescriptionsDataBo> prescriptions = fetchPrescriptionsDataByDni.run(identificationNumber);
		return prescriptions.stream()
				.map(prescriptionMapper::mapToPrescriptionsDataDto)
				.collect(Collectors.toList());
	}
}
