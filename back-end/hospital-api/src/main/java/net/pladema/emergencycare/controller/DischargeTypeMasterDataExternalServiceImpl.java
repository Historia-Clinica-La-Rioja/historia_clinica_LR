package net.pladema.emergencycare.controller;

import net.pladema.emergencycare.controller.mapper.DischargeTypeMasterDataMapper;
import net.pladema.emergencycare.service.DischargeTypeMasterDataService;
import ar.lamansys.sgx.shared.masterdata.infrastructure.input.rest.dto.MasterDataDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Collection;

@Service
public class DischargeTypeMasterDataExternalServiceImpl implements DischargeTypeMasterDataExternalService {

    private static final Logger LOG = LoggerFactory.getLogger(DischargeTypeMasterDataExternalServiceImpl.class);

    private final DischargeTypeMasterDataService dischargeTypeMasterDataService;

    private final DischargeTypeMasterDataMapper dischargeTypeMasterDataMapper;

    public DischargeTypeMasterDataExternalServiceImpl(DischargeTypeMasterDataService dischargeTypeMasterDataService,
                                                      DischargeTypeMasterDataMapper dischargeTypeMasterDataMapper) {
        this.dischargeTypeMasterDataService = dischargeTypeMasterDataService;
        this.dischargeTypeMasterDataMapper = dischargeTypeMasterDataMapper;
    }

    public Collection<MasterDataDto> internmentGetOf() {
        LOG.debug("No input parameters");
        return dischargeTypeMasterDataMapper.fromListDischargeTypeBo(dischargeTypeMasterDataService.getAllInternmentDischargeTypes());
    }

    public Collection<MasterDataDto> emergencyCareGetOf() {
        LOG.debug("No input parameters");
        return dischargeTypeMasterDataMapper.fromListDischargeTypeBo(dischargeTypeMasterDataService.getAllEmergencyCareDischargeTypes());
    }

}
