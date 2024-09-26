package net.pladema.medicine.infrastructure.input.rest;


import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import net.pladema.medicine.application.GetAuditRequiredMedicineGroups;
import net.pladema.medicine.infrastructure.input.rest.dto.MedicineGroupAuditRequiredDto;
import net.pladema.medicine.infrastructure.input.rest.mapper.MedicineGroupAuditRequiredMapper;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@Validated
@RequiredArgsConstructor
@Tag(name = "Medicine Group", description = "Medicine Group")
@RequestMapping("/institutions/{institutionId}/medicine-group")
@RestController
public class MedicineGroupController {

	public static final String OUTPUT = "Output -> {}";

	private final GetAuditRequiredMedicineGroups getAuditRequiredMedicineGroups;
	private final MedicineGroupAuditRequiredMapper medicineGroupAuditRequiredMapper;

	@GetMapping("medicine/{medicineId}/problem/{problemId}")
	public ResponseEntity<List<MedicineGroupAuditRequiredDto>> getAllMedicineGroup(@PathVariable(name = "medicineId") String medicineId,
																				   @PathVariable(name = "problemId") String problemId) {
		log.debug("Input parameters -> medicationId {}, problemId {}", medicineId, problemId);
		List<MedicineGroupAuditRequiredDto> result = medicineGroupAuditRequiredMapper.toMedicineGroupAuditRequiredDto(getAuditRequiredMedicineGroups.run(medicineId, problemId));
		log.debug(OUTPUT, result);
		return ResponseEntity.ok(result);
	}
}
