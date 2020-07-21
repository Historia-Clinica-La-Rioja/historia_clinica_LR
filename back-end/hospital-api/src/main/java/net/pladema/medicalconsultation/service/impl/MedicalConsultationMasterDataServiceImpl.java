package net.pladema.medicalconsultation.service.impl;

import net.pladema.medicalconsultation.service.MedicalConsultationMasterDataService;
import net.pladema.sgx.masterdata.repository.MasterDataProjection;
import net.pladema.sgx.masterdata.repository.MasterdataRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Collection;

@Service
public class MedicalConsultationMasterDataServiceImpl implements MedicalConsultationMasterDataService {

    private static final Logger LOG = LoggerFactory.getLogger(MedicalConsultationMasterDataServiceImpl.class);

    private final MasterdataRepository masterdataRepository;

    public MedicalConsultationMasterDataServiceImpl(MasterdataRepository masterdataRepository) {
        this.masterdataRepository = masterdataRepository;
    }

    @Override
    public <T> Collection<MasterDataProjection> findAll(Class<T> clazz, String...filterIds) {
        LOG.debug("Input parameters -> {}, filterIds {}", clazz, filterIds);
        return masterdataRepository.findAllProjectedBy(clazz, filterIds);
    }
}
