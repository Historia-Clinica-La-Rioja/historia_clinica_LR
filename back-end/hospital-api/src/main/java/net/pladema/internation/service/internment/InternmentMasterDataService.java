package net.pladema.internation.service.internment;

import net.pladema.internation.repository.projections.InternmentMasterDataProjection;

import java.util.Collection;

public interface InternmentMasterDataService {

    public <T> Collection<InternmentMasterDataProjection> findAll(Class<T> clazz, String...filterIds);
}
