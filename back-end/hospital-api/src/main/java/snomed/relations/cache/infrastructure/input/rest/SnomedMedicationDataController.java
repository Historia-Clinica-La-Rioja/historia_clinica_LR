package snomed.relations.cache.infrastructure.input.rest;

import ar.lamansys.sgh.shared.infrastructure.input.service.SharedSnomedDto;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

import lombok.extern.slf4j.Slf4j;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import snomed.relations.cache.application.fetchcommercialmedications.FetchCommercialMedications;
import snomed.relations.cache.application.getCommercialMedicationDosageFormUnitValues.GetCommercialMedicationDosageFormUnitValues;
import snomed.relations.cache.application.getMedicationPresentationUnits.GetMedicationPresentationUnits;
import snomed.relations.cache.infrastructure.input.rest.dto.CommercialMedicationDto;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Tag(name = "Snomed medication data")
@RequestMapping("/institutions/{institutionId}/snomed-medication")
@RestController
public class SnomedMedicationDataController {

	private final FetchCommercialMedications fetchCommercialMedications;

	private final GetMedicationPresentationUnits getMedicationPresentationUnits;

	private final GetCommercialMedicationDosageFormUnitValues getCommercialMedicationDosageFormUnitValues;

	@GetMapping("/commercials")
	@PreAuthorize("hasPermission(#institutionId, 'PRESCRIPTOR, ESPECIALISTA_MEDICO, ESPECIALISTA_EN_ODONTOLOGIA, PROFESIONAL_DE_SALUD, ENFERMERO')")
	public ResponseEntity<List<CommercialMedicationDto>> getCommercialMedications(@PathVariable(name = "institutionId") Integer institutionId) {
		var result = fetchCommercialMedications.run()
				.stream()
				.map(cm -> new CommercialMedicationDto(new SharedSnomedDto(cm.getCommercial().getSctid(), cm.getCommercial().getPt()),
						new SharedSnomedDto(cm.getGeneric().getSctid(), cm.getGeneric().getPt())))
				.collect(Collectors.toList());;
		return ResponseEntity.ok(result);
	}

	@GetMapping("/{medicationSctid}/get-presentation-units")
	@PreAuthorize("hasPermission(#institutionId, 'PRESCRIPTOR, ESPECIALISTA_MEDICO, ESPECIALISTA_EN_ODONTOLOGIA, PROFESIONAL_DE_SALUD, ENFERMERO')")
	public List<Integer> getMedicationPresentationUnits(@PathVariable("institutionId") Integer institutionId,
														@PathVariable("medicationSctid") String medicationSctid) {
		log.debug("Input parameters -> institutionId {}, medicationSctid {}", institutionId, medicationSctid);
		List<Integer> result = getMedicationPresentationUnits.run(medicationSctid);
		log.debug("Output -> {}", result);
		return result;
	}

	@GetMapping("/{genericSctid}/get-commercial-medication-dosage-form-units")
	@PreAuthorize("hasPermission(#institutionId, 'PRESCRIPTOR, ESPECIALISTA_MEDICO, ESPECIALISTA_EN_ODONTOLOGIA, PROFESIONAL_DE_SALUD, ENFERMERO')")
	public List<String> getCommercialMedicationDosageFormUnits(@PathVariable("institutionId") Integer institutionId,
															   @PathVariable("genericSctid") String genericSctid) {
		log.debug("Input parameters -> institutionId {}, medicationSctid {}", institutionId, genericSctid);
		List<String> result = getCommercialMedicationDosageFormUnitValues.run(genericSctid);
		log.debug("Output -> {}", result);
		return result;
	}

}
