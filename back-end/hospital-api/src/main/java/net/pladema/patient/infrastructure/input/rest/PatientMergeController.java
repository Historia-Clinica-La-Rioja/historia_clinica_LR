package net.pladema.patient.infrastructure.input.rest;

import net.pladema.patient.application.unmergepatient.UnmergePatient;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.pladema.patient.application.mergepatient.MergePatient;
import net.pladema.patient.controller.dto.PatientToMergeDto;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/patient-merge/institution/{institutionId}")
@Tag(name = "Patient merge", description = "Patient merge")
public class PatientMergeController {


	private final MergePatient mergePatient;

	private final UnmergePatient unmergePatient;

	@PostMapping("/merge")
	@Transactional
	@PreAuthorize("hasPermission(#institutionId, 'ADMINISTRATIVO, ADMINISTRATIVO_RED_DE_IMAGENES') || hasAnyAuthority('AUDITOR_MPI')")
	public ResponseEntity<Integer> merge(@PathVariable(name = "institutionId") Integer institutionId, @RequestBody PatientToMergeDto patientToMerge) {
		log.debug("Input parameters -> institutionId {}, patientToMerge {}", institutionId, patientToMerge);
		Integer result = mergePatient.run(institutionId,  patientToMerge);
		log.debug("Output result -> paciente activado {}", result);
		return ResponseEntity.ok().body(result);
	}

	@PostMapping("/unmerge")
	@Transactional
	@PreAuthorize("hasAnyAuthority('AUDITOR_MPI')")
	public ResponseEntity<Boolean> unmerge(@PathVariable(name = "institutionId") Integer institutionId,  @RequestBody PatientToMergeDto patientToUnmerge) {
		log.debug("Input parameters -> institutionId {}, patientToUnmerge {}, ", institutionId, patientToUnmerge);
		Boolean result = unmergePatient.run(institutionId, patientToUnmerge);
		log.debug("Output result -> listado de pacientes reactivados {}", result);
		return ResponseEntity.ok().body(result);
	}


}