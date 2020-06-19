package net.pladema.sgx.masterdata.repository;

import java.util.Collection;

public interface MasterdataRepository {

    <T> Collection<MasterDataProjection> findAllInternmentProjectedBy(Class<T> clazz, String...filterIds);
}
