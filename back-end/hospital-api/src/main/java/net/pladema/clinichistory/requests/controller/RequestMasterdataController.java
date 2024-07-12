package net.pladema.clinichistory.requests.controller;

import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.document.entity.EProfessionType;
import ar.lamansys.sgx.shared.masterdata.infrastructure.input.rest.dto.GenericMasterDataDto;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.pladema.clinichistory.requests.controller.dto.ServiceRequestCategoryDto;
import net.pladema.clinichistory.requests.service.GetServiceRequestCategoriesService;
import net.pladema.clinichistory.requests.service.domain.EDiagnosticReportStatus;
import net.pladema.clinichistory.requests.service.domain.EMedicationStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Tag(name = "Requests Master Data", description = "Requests Master Data")
@RequestMapping("/requests/masterdata")
@Slf4j
@RequiredArgsConstructor
@RestController
public class RequestMasterdataController {

	private final GetServiceRequestCategoriesService getServiceRequestCategoriesService;

    @GetMapping(value = "/categories")
    public ResponseEntity<Collection<ServiceRequestCategoryDto>> categories(){
        log.debug("{}", "All request categories");
		List<ServiceRequestCategoryDto> result = getServiceRequestCategoriesService.run()
				.stream()
				.map(src -> new ServiceRequestCategoryDto(src.getId(), src.getDescription()))
				.collect(Collectors.toList());
        log.debug("OUTPUT -> {}", result);
        return ResponseEntity.ok().body(result);
    }

    @GetMapping(value = "/medication-status")
    public ResponseEntity<Collection<EMedicationStatus>> medicationStatusFromEnum(){
        log.debug("{}", "All allergy intolerance category");
        Collection<EMedicationStatus> result = EMedicationStatus.getAll();
        log.debug("OUTPUT -> {}", result);
        return ResponseEntity.ok().body(result);
    }

    @GetMapping(value = "/diagnostic-report-status")
    public ResponseEntity<Collection<EDiagnosticReportStatus>> diagnosticReportStatus(){
        log.debug("{}", "All allergy intolerance category");
        Collection<EDiagnosticReportStatus> result = EDiagnosticReportStatus.getAll();
        log.debug("OUTPUT -> {}", result);
        return ResponseEntity.ok().body(result);
    }

	@GetMapping(value = "/get-surgical-report-profession-types")
	public ResponseEntity<List<GenericMasterDataDto<EProfessionType>>> getSurgicalReportProfessionTypes(){
		log.debug("{}", "Get all surgical report's profession types");
		List<EProfessionType> surgicalReportProfessions = EProfessionType.getSurgicalTeam();
		List<GenericMasterDataDto<EProfessionType>> result = surgicalReportProfessions.stream()
                .map(this::createProfessionTypeGenericMasterDataDto)
                .collect(Collectors.toList());
		log.debug("OUTPUT -> {}", result);
		return ResponseEntity.ok().body(result);
	}

	private GenericMasterDataDto<EProfessionType> createProfessionTypeGenericMasterDataDto(EProfessionType professionType) {
		return new GenericMasterDataDto<>(professionType, professionType.getDescription());
	}

}
