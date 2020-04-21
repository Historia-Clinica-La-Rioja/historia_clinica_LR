package net.pladema.internation.service.documents.anamnesis.impl;

import net.pladema.internation.controller.dto.ips.VitalSignDto;
import net.pladema.internation.service.documents.anamnesis.CreateAnamnesisService;
import net.pladema.internation.service.domain.Anamnesis;
import net.pladema.internation.service.domain.ips.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CreateAnamnesisServiceImpl implements CreateAnamnesisService {

    private final Logger LOG = LoggerFactory.getLogger(this.getClass());

    public CreateAnamnesisServiceImpl() {
    }


    @Override
    public Anamnesis createAnanmesisDocument(Anamnesis anamnesis) {
        LOG.debug("Input parameters {}", anamnesis);
        return null;
    }

    private void loadDiagnosis(List<HealthCondition> diagnosis) {
        //TODO
    }

    private void loadPersonalHistories(List<HealthHistoryCondition> personalHistories) {
        //TODO
    }

    private void loadFamilyHistories(List<HealthHistoryCondition> familyHistories) {
        //TODO
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
