package net.pladema.internation.service.documents.anamnesis.impl;

import net.pladema.internation.repository.masterdata.entity.DocumentStatus;
import net.pladema.internation.service.documents.DocumentService;
import net.pladema.internation.service.documents.anamnesis.AnamnesisService;
import net.pladema.internation.service.domain.Anamnesis;
import net.pladema.internation.service.domain.ips.GeneralHealthConditionBo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class AnamnesisServiceImpl implements AnamnesisService {

    private static final Logger LOG = LoggerFactory.getLogger(AnamnesisServiceImpl.class);

    public static final String OUTPUT = "Output -> {}";

    private final DocumentService documentService;

    public AnamnesisServiceImpl(DocumentService documentService) {
        this.documentService = documentService;
    }

    @Override
    public Anamnesis getAnamnesis(Long anamnesisId) {
        LOG.debug("Input parameters {}", anamnesisId);
        Anamnesis result = new Anamnesis();
        documentService.findById(anamnesisId).ifPresent( document -> {
            result.setId(document.getId());
            result.setConfirmed(document.getStatusId().equalsIgnoreCase(DocumentStatus.FINAL));

            GeneralHealthConditionBo generalHealthConditionBo = documentService.getHealthConditionFromDocument(document.getId());
            result.setDiagnosis(generalHealthConditionBo.getDiagnosis());
            result.setFamilyHistories(generalHealthConditionBo.getFamilyHistories());
            result.setPersonalHistories(generalHealthConditionBo.getPersonalHistories());

            result.setMedications(documentService.getMedicationStateFromDocument(document.getId()));
            result.setInmunizations(documentService.getInmunizationStateFromDocument(document.getId()));
            result.setAllergies(documentService.getAllergyIntoleranceStateFromDocument(document.getId()));
            result.setAnthropometricData(documentService.getAnthropometricDataStateFromDocument(document.getId()));
            result.setVitalSigns(documentService.getVitalSignStateFromDocument(document.getId()));
        });
        LOG.debug(OUTPUT, result);
        return result;
    }
}
