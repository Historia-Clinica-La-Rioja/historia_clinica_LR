package net.pladema.emergencycare.service.impl;

import net.pladema.emergencycare.service.EmergencyCareMasterDataService;
import net.pladema.emergencycare.service.domain.enums.EEmergencyCareEntrance;
import net.pladema.emergencycare.service.domain.enums.EEmergencyCareType;
import net.pladema.sgx.masterdata.repository.MasterDataProjection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Collection;

@Service
public class EmergencyCareMasterDataServiceImpl implements EmergencyCareMasterDataService {

    private static final Logger LOG = LoggerFactory.getLogger(EmergencyCareMasterDataServiceImpl.class);

    public static final String OUTPUT = "Output -> {}";

    public EmergencyCareMasterDataServiceImpl(){
        super();
    }

    @Override
    public Collection<EEmergencyCareType> findAllType() {
        Collection<EEmergencyCareType> result = EEmergencyCareType.getAll();
        LOG.debug(OUTPUT, result);
        return result;
    }

    @Override
    public Collection<EEmergencyCareEntrance> findAllEntrance() {
        Collection<EEmergencyCareEntrance> result =  EEmergencyCareEntrance.getAll();
        LOG.debug(OUTPUT, result);
        return result;
    }
}
