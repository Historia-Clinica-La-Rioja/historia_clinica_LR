package net.pladema.clinichistory.requests.controller.dto;

import io.swagger.annotations.Api;
import net.pladema.clinichistory.requests.service.domain.EDiagnosticReportStatus;
import net.pladema.clinichistory.requests.service.domain.EMedicationStatus;
import net.pladema.clinichistory.requests.servicerequests.repository.entity.ServiceRequestCategory;
import ar.lamansys.sgx.shared.masterdata.infrastructure.output.repository.MasterDataProjection;
import ar.lamansys.sgx.shared.masterdata.application.MasterDataService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;

@RestController
@RequestMapping("/requests/masterdata")
@Api(value = "Requests Master Data", tags = { "Requests Master Data" })
public class RequestMasterdataController {

    private static final Logger LOG = LoggerFactory.getLogger(RequestMasterdataController.class);

    private final MasterDataService masterDataService;

    public RequestMasterdataController(MasterDataService masterDataService){
        super();
        this.masterDataService = masterDataService;
    }

    @GetMapping(value = "/categories")
    public ResponseEntity<Collection<MasterDataProjection>> categories(){
        LOG.debug("{}", "All request categories");
        Collection<MasterDataProjection> result = masterDataService.findAll(ServiceRequestCategory.class);
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
