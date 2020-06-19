package net.pladema.internation.service.internment;

import net.pladema.sgx.masterdata.repository.MasterDataProjection;

import java.util.Collection;

public interface InternmentMasterDataService {

    <T> Collection<MasterDataProjection> findAll(Class<T> clazz, String...filterIds);
}
