package net.pladema.clinichistory.requests.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import net.pladema.clinichistory.requests.controller.dto.ServiceRequestCategoryDto;
import net.pladema.clinichistory.requests.service.GetServiceRequestCategoriesService;
import net.pladema.clinichistory.requests.service.domain.EDiagnosticReportStatus;
import net.pladema.clinichistory.requests.service.domain.EMedicationStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/requests/masterdata")
@Tag(name = "Requests Master Data", description = "Requests Master Data")
public class RequestMasterdataController {

    private static final Logger LOG = LoggerFactory.getLogger(RequestMasterdataController.class);

	private final GetServiceRequestCategoriesService getServiceRequestCategoriesService;

    public RequestMasterdataController(GetServiceRequestCategoriesService getServiceRequestCategoriesService){
        super();
		this.getServiceRequestCategoriesService = getServiceRequestCategoriesService;
	}

    @GetMapping(value = "/categories")
    public ResponseEntity<Collection<ServiceRequestCategoryDto>> categories(){
        LOG.debug("{}", "All request categories");
		List<ServiceRequestCategoryDto> result = getServiceRequestCategoriesService.run()
				.stream()
				.map(src -> new ServiceRequestCategoryDto(src.getId(), src.getDescription()))
				.collect(Collectors.toList());
        LOG.debug("OUTPUT -> {}", result);
        return ResponseEntity.ok().body(result);
    }

    @GetMapping(value = "/medication-status")
    public ResponseEntity<Collection<EMedicationStatus>> medicationStatusFromEnum(){
        LOG.debug("{}", "All allergy intolerance category");
        Collection<EMedicationStatus> result = EMedicationStatus.getAll();
        LOG.debug("OUTPUT -> {}", result);
        return ResponseEntity.ok().body(result);
    }

    @GetMapping(value = "/diagnostic-report-status")
    public ResponseEntity<Collection<EDiagnosticReportStatus>> diagnosticReportStatus(){
        LOG.debug("{}", "All allergy intolerance category");
        Collection<EDiagnosticReportStatus> result = EDiagnosticReportStatus.getAll();
        LOG.debug("OUTPUT -> {}", result);
        return ResponseEntity.ok().body(result);
    }
}
