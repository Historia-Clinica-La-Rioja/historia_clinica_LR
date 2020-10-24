package net.pladema.clinichistory.hospitalization.service.masterdata;

import net.pladema.sgx.masterdata.repository.MasterDataProjection;

import java.util.Collection;

public interface InternmentMasterDataService {

    <T> Collection<MasterDataProjection> findAll(Class<T> clazz, String...filterIds);

    <T> Collection<MasterDataProjection> findAllRestrictedBy(Class<T> clazz, String field, Short flag);
}
