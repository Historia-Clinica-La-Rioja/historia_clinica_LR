package net.pladema.emergencycare.controller;

import io.swagger.annotations.Api;
import net.pladema.emergencycare.service.EmergencyCareMasterDataService;
import net.pladema.emergencycare.service.domain.enums.EEmergencyCareEntrance;
import net.pladema.emergencycare.service.domain.enums.EEmergencyCareType;
import net.pladema.sgx.masterdata.repository.MasterDataProjection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;

@RestController
@RequestMapping("/emergency-care/masterdata")
@Api(value = "Emergency care Master data", tags = { "Emergency care Master data" })
public class EmergencyCareMasterDataController {

    private static final Logger LOG = LoggerFactory.getLogger(EmergencyCareMasterDataController.class);

    private final EmergencyCareMasterDataService emergencyCareMasterDataService;

    public EmergencyCareMasterDataController(EmergencyCareMasterDataService emergencyCareMasterDataService){
        super();
        this.emergencyCareMasterDataService=emergencyCareMasterDataService;
    }

    @GetMapping(value = "/type")
    public ResponseEntity<Collection<EEmergencyCareType>> getType() {
        LOG.debug("{}", "All types");
        return ResponseEntity.ok().body(emergencyCareMasterDataService.findAllType());
    }

    @GetMapping(value = "/entranceType")
    public ResponseEntity<Collection<EEmergencyCareEntrance>> getEntranceType() {
        LOG.debug("{}", "All entrance types");
        return ResponseEntity.ok().body(emergencyCareMasterDataService.findAllEntrance());
    }
}
