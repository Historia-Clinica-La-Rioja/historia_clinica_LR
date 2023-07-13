package net.pladema.clinichistory.requests.servicerequests.infrastructure.input.rest;

import ar.lamansys.sgx.shared.dates.configuration.LocalDateMapper;
import ar.lamansys.sgx.shared.masterdata.domain.EnumWriter;
import ar.lamansys.sgx.shared.masterdata.infrastructure.input.rest.dto.MasterDataDto;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import net.pladema.clinichistory.requests.servicerequests.application.GetWorklist;
import net.pladema.clinichistory.requests.servicerequests.domain.WorklistBo;
import net.pladema.clinichistory.requests.servicerequests.infrastructure.input.rest.dto.WorklistDto;

import net.pladema.clinichistory.requests.servicerequests.infrastructure.input.service.EDiagnosticImageReportStatus;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/institutions/{institutionId}/worklist")
@Tag(name = "Worklist", description = "Worklist")
public class WorklistController {

	private final GetWorklist getWorklist;
	private final LocalDateMapper localDateMapper;

	@GetMapping(value = "/by-modality")
	@PreAuthorize("hasPermission(#institutionId, 'INFORMADOR')")
	public ResponseEntity<List<WorklistDto>> getWorklistByModalityAndInstitution(
			@PathVariable(name = "institutionId") Integer institutionId,
			@RequestParam(name = "modalityId", required = false) Integer modalityId
	) {
		log.debug("Input parameters -> institutionId {}, modalityId {}", institutionId, modalityId);
		List<WorklistBo> worklistBo = getWorklist.run(modalityId, institutionId);
		List<WorklistDto> result = worklistBo.stream().map(this::mapToWorklistDto).collect(Collectors.toList());
		log.debug("Get worklist by modality and institution {}", result);
		return ResponseEntity.ok().body(result);
	}

	@GetMapping(value = "/informer-status")
	public ResponseEntity<Collection<MasterDataDto>> getStatus() {
		log.debug("All diagnostic image report status in informer worklist");
		return ResponseEntity.ok().body(EnumWriter.writeList(EDiagnosticImageReportStatus.getAll()));
	}

	private WorklistDto mapToWorklistDto(WorklistBo bo) {
		return new WorklistDto(
				bo.getPatientId(),
				bo.getPatientIdentificationTypeId(),
				bo.getPatientIdentificationNumber(),
				bo.getPatientFullName(),
				bo.getStatusId(),
				bo.getAppointmentId(),
				localDateMapper.toDateTimeDto(bo.getActionTime())
		);
	}
}
