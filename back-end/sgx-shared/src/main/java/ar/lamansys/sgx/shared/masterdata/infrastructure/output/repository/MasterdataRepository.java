package ar.lamansys.sgx.shared.masterdata.infrastructure.output.repository;

import java.util.Collection;

public interface MasterdataRepository {

    <T> Collection<MasterDataProjection> findAllProjectedBy(Class<T> clazz, String...filterIds);

    <T> Collection<MasterDataProjection> findAllRestrictedBy(Class<T> clazz, String field, Short flag);
}
