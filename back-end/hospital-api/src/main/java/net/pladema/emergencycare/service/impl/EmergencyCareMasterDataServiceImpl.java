package net.pladema.emergencycare.service.impl;

import net.pladema.emergencycare.service.EmergencyCareMasterDataService;
import net.pladema.sgx.masterdata.repository.MasterDataProjection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;

@Service
public class EmergencyCareMasterDataServiceImpl implements EmergencyCareMasterDataService {

    private static final Logger LOG = LoggerFactory.getLogger(EmergencyCareMasterDataServiceImpl.class);

    public EmergencyCareMasterDataServiceImpl(){
        super();
    }

    @Override
    public <T> Collection<MasterDataProjection> findAll(Class<T> clazz) {
        //TODO implement method
        return new ArrayList<>();
    }
}
