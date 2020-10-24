package net.pladema.clinichistory.hospitalization.service.masterdata;

import net.pladema.sgx.masterdata.repository.MasterDataProjection;
import net.pladema.sgx.masterdata.repository.MasterdataRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Collection;

@Service
public class InternmentMasterDataServiceImpl implements InternmentMasterDataService {

    private static final Logger LOG = LoggerFactory.getLogger(InternmentMasterDataServiceImpl.class);

    private final MasterdataRepository masterdataRepository;

    public InternmentMasterDataServiceImpl(MasterdataRepository masterdataRepository) {
        this.masterdataRepository = masterdataRepository;
    }

    @Override
    public <T> Collection<MasterDataProjection> findAll(Class<T> clazz, String...filterIds) {
        LOG.debug("Input parameters -> {}", clazz);
        return masterdataRepository.findAllProjectedBy(clazz, filterIds);
    }

    @Override
    public <T> Collection<MasterDataProjection> findAllRestrictedBy(Class<T> clazz, String field, Short flag) {
        LOG.debug("Input parameters -> {}", clazz);
        return masterdataRepository.findAllRestrictedBy(clazz, field, flag);
    }
}
