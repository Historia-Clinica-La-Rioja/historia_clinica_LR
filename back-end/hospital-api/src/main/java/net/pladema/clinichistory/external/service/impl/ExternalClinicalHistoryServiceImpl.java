package net.pladema.clinichistory.external.service.impl;

import net.pladema.clinichistory.external.repository.ExternalClinicalHistoryRepository;
import net.pladema.clinichistory.external.service.ExternalClinicalHistoryService;
import net.pladema.clinichistory.external.service.domain.ExternalClinicalHistoryBo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ExternalClinicalHistoryServiceImpl implements ExternalClinicalHistoryService {
    private static final Logger LOG = LoggerFactory.getLogger(ExternalClinicalHistoryServiceImpl.class);

    public static final String OUTPUT = "Output -> {}";

    private final ExternalClinicalHistoryRepository externalClinicalHistoryRepository;

    public ExternalClinicalHistoryServiceImpl(ExternalClinicalHistoryRepository externalClinicalHistoryRepository) {
        this.externalClinicalHistoryRepository = externalClinicalHistoryRepository;
    }

    @Override
    public List<ExternalClinicalHistoryBo> getExternalClinicalHistory(Integer patientId) {
        LOG.debug("Input parameter -> patientId {}", patientId);
        List<ExternalClinicalHistoryBo> result = externalClinicalHistoryRepository.getAllExternalClinicalHistory(patientId).stream().map(ExternalClinicalHistoryBo::new)
                .collect(Collectors.toList());
        LOG.debug("Output -> {}", result);
        return result;
    }
}
