package net.pladema.emergencycare.controller;

import io.swagger.annotations.Api;
import net.pladema.emergencycare.controller.mapper.DischargeTypeMasterDataMapper;
import net.pladema.emergencycare.service.DischargeTypeMasterDataService;
import net.pladema.emergencycare.service.EmergencyCareMasterDataService;
import net.pladema.sgx.masterdata.dto.MasterDataDto;
import net.pladema.sgx.masterdata.service.domain.EnumWriter;
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

    private final DischargeTypeMasterDataService dischargeTypeMasterDataService;

    private final DischargeTypeMasterDataMapper dischargeTypeMasterDataMapper;

    public EmergencyCareMasterDataController(EmergencyCareMasterDataService emergencyCareMasterDataService,
                                             DischargeTypeMasterDataService dischargeTypeMasterDataService,
                                             DischargeTypeMasterDataMapper dischargeTypeMasterDataMapper){
        super();
        this.emergencyCareMasterDataService=emergencyCareMasterDataService;
        this.dischargeTypeMasterDataService = dischargeTypeMasterDataService;
        this.dischargeTypeMasterDataMapper = dischargeTypeMasterDataMapper;
    }

    @GetMapping(value = "/type")
    public ResponseEntity<Collection<MasterDataDto>> getType() {
        LOG.debug("{}", "All types");
        return ResponseEntity.ok().body(EnumWriter.writeList(emergencyCareMasterDataService.findAllType()));
    }

    @GetMapping(value = "/entranceType")
    public ResponseEntity<Collection<MasterDataDto>> getEntranceType() {
        LOG.debug("{}", "All entrance types");
        return ResponseEntity.ok().body(EnumWriter.writeList(emergencyCareMasterDataService.findAllEntrance()));
    }

    @GetMapping(value = "/dischargeType")
    public ResponseEntity<Collection<MasterDataDto>> getDischargeType() {
        LOG.debug("{}", "All emergency care discharge types");
        return ResponseEntity.ok().body(dischargeTypeMasterDataMapper
                .fromListDischargeTypeBo(dischargeTypeMasterDataService.getAllEmergencyCareDischargeTypes()));
    }
}
