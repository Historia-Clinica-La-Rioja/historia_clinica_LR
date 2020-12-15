package net.pladema.emergencycare.service;

import net.pladema.sgx.masterdata.repository.MasterDataProjection;

import java.util.Collection;

public interface EmergencyCareMasterDataService {

    <T> Collection<MasterDataProjection> findAll(Class<T> clazz);
}
