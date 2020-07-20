package net.pladema.sgx.masterdata.repository;

import java.util.Collection;

public interface MasterdataRepository {

    <T> Collection<MasterDataProjection> findAllProjectedBy(Class<T> clazz, String...filterIds);
}
