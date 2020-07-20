package net.pladema.appointment.service;

import net.pladema.sgx.masterdata.repository.MasterDataProjection;

import java.util.Collection;

public interface AppointmentMasterDataService {

    <T> Collection<MasterDataProjection> findAll(Class<T> clazz, String...filterIds);
}
