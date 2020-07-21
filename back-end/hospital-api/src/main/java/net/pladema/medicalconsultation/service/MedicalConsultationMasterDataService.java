package net.pladema.medicalconsultation.service;

import net.pladema.sgx.masterdata.repository.MasterDataProjection;

import java.util.Collection;

public interface MedicalConsultationMasterDataService {

    <T> Collection<MasterDataProjection> findAll(Class<T> clazz, String...filterIds);
}
