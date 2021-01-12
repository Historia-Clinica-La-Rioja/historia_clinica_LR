package net.pladema.emergencycare.service.impl;

import net.pladema.emergencycare.service.EmergencyCareMasterDataService;
import net.pladema.emergencycare.service.domain.enums.EEmergencyCareEntrance;
import net.pladema.emergencycare.service.domain.enums.EEmergencyCareType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EmergencyCareMasterDataServiceImpl implements EmergencyCareMasterDataService {

    private static final Logger LOG = LoggerFactory.getLogger(EmergencyCareMasterDataServiceImpl.class);

    public static final String OUTPUT = "Output -> {}";

    public EmergencyCareMasterDataServiceImpl(){
        super();
    }

    @Override
    public List<EEmergencyCareType> findAllType() {
        List<EEmergencyCareType> result = EEmergencyCareType.getAll();
        LOG.debug(OUTPUT, result);
        return result;
    }

    @Override
    public List<EEmergencyCareEntrance> findAllEntrance() {
        List<EEmergencyCareEntrance> result =  EEmergencyCareEntrance.getAll();
        LOG.debug(OUTPUT, result);
        return result;
    }
}
