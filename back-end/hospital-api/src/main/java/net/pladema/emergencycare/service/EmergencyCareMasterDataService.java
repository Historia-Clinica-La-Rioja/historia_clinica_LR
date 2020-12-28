package net.pladema.emergencycare.service;

import net.pladema.emergencycare.service.domain.enums.EEmergencyCareEntrance;
import net.pladema.emergencycare.service.domain.enums.EEmergencyCareType;
import net.pladema.sgx.masterdata.repository.MasterDataProjection;

import java.util.Collection;

public interface EmergencyCareMasterDataService {

    Collection<EEmergencyCareType> findAllType();

    Collection<EEmergencyCareEntrance> findAllEntrance();
}
