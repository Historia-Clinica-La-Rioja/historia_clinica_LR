package ar.lamansys.sgx.shared.masterdata.application;

import ar.lamansys.sgx.shared.masterdata.infrastructure.output.repository.MasterDataProjection;
import ar.lamansys.sgx.shared.masterdata.infrastructure.output.repository.MasterdataRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Collection;

@Service
public class MasterDataServiceImpl implements MasterDataService {

    private static final Logger LOG = LoggerFactory.getLogger(MasterDataServiceImpl.class);

    private final MasterdataRepository masterdataRepository;

    public MasterDataServiceImpl(MasterdataRepository masterdataRepository) {
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
