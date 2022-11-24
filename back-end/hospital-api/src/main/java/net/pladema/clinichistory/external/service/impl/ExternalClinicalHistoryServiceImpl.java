package net.pladema.clinichistory.external.service.impl;

import net.pladema.clinichistory.external.repository.ExternalClinicalHistoryRepository;
import net.pladema.clinichistory.external.service.ExternalClinicalHistoryService;
import net.pladema.clinichistory.external.service.domain.ExternalClinicalHistorySummaryBo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ExternalClinicalHistoryServiceImpl implements ExternalClinicalHistoryService {
    private static final Logger LOG = LoggerFactory.getLogger(ExternalClinicalHistoryServiceImpl.class);

    public static final String OUTPUT = "Output -> {}";

    private final ExternalClinicalHistoryRepository externalClinicalHistoryRepository;

    public ExternalClinicalHistoryServiceImpl(ExternalClinicalHistoryRepository externalClinicalHistoryRepository) {
        this.externalClinicalHistoryRepository = externalClinicalHistoryRepository;
    }

    @Override
    public List<ExternalClinicalHistorySummaryBo> getExternalClinicalHistory(Integer patientId) {
        LOG.debug("Input parameter -> patientId {}", patientId);
        List<ExternalClinicalHistorySummaryBo> result = externalClinicalHistoryRepository.getAllExternalClinicalHistory(patientId);
        LOG.debug("Output -> {}", result);
        return result;
    }
}
