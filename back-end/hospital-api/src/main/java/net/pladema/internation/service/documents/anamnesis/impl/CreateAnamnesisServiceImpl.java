package net.pladema.internation.service.documents.anamnesis.impl;

import net.pladema.internation.controller.dto.ips.VitalSignDto;
import net.pladema.internation.service.documents.anamnesis.CreateAnamnesisService;
import net.pladema.internation.service.documents.anamnesis.HealthConditionService;
import net.pladema.internation.service.domain.Anamnesis;
import net.pladema.internation.service.domain.ips.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CreateAnamnesisServiceImpl implements CreateAnamnesisService {

    private final Logger LOG = LoggerFactory.getLogger(this.getClass());

    private final HealthConditionService healthConditionService;

    public CreateAnamnesisServiceImpl(HealthConditionService healthConditionService) {
        this.healthConditionService = healthConditionService;
    }

    @Override
    public Anamnesis createAnanmesisDocument(Integer IntermentEpisodeId, Integer patientId, Anamnesis anamnesis) {
        LOG.debug("Input parameters -> intermentEpisodeId {}, patientId {}, anamnesis {}", anamnesis);

        //Todo: reemplazar la línea siguiente
        Long documentId = 2L;
        healthConditionService.loadDiagnosis(patientId, documentId, anamnesis.getDiagnosis());
        healthConditionService.loadPersonalHistories(patientId, documentId, anamnesis.getPersonalHistory());
        healthConditionService.loadFamilyHistories(patientId, documentId, anamnesis.getFamilyHistory());

        return anamnesis;
    }

    private void loadAllergies(List<HealthHistoryCondition> allergies) {
        //TODO
    }

    private void loadInmunizations(List<Inmunization> inmunizations) {
        //TODO
    }

    private void loadMedications(List<Medication> medications) {
        //TODO
    }

    private void loadNotes(Observations notes) {
        //TODO
    }

    private void loadVitalSigns(VitalSignDto vitalSigns) {
        //TODO
    }

    private void loadAnthropometricData(AnthropometricData anthropometricData) {

    }

}
