package net.pladema.internation.service.impl;

import net.pladema.internation.repository.projections.InternmentMasterDataProjection;
import net.pladema.internation.service.InternmentMasterDataService;
import net.pladema.masterdata.repository.MasterdataRepository;
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
    public <T> Collection<InternmentMasterDataProjection> findAll(Class<T> clazz) {
        LOG.debug("Input parameters -> {}", clazz);
        return masterdataRepository.findAllInternmentProjectedBy(clazz);
    }
}
