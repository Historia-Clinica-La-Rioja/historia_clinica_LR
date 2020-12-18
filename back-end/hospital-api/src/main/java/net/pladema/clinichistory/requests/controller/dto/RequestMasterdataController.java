package net.pladema.clinichistory.requests.controller.dto;

import io.swagger.annotations.Api;
import net.pladema.clinichistory.requests.servicerequests.repository.entity.ServiceRequestCategory;
import net.pladema.sgx.masterdata.repository.MasterDataProjection;
import net.pladema.sgx.masterdata.service.MasterDataService;
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

    @GetMapping(value = "/request/categories")
    public ResponseEntity<Collection<MasterDataProjection>> getAllergyCategory(){
        LOG.debug("{}", "All allergy intolerance category");
        return ResponseEntity.ok().body(masterDataService.findAll(ServiceRequestCategory.class));
    }
    
    
}
