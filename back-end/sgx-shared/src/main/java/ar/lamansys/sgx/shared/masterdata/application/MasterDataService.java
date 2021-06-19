package ar.lamansys.sgx.shared.masterdata.application;

import ar.lamansys.sgx.shared.masterdata.infrastructure.output.repository.MasterDataProjection;

import java.util.Collection;

public interface MasterDataService {

    <T> Collection<MasterDataProjection> findAll(Class<T> clazz, String...filterIds);

    <T> Collection<MasterDataProjection> findAllRestrictedBy(Class<T> clazz, String field, Short flag);
}
