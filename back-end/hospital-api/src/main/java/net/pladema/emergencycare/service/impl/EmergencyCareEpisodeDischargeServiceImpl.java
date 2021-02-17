package net.pladema.emergencycare.service.impl;

import net.pladema.clinichistory.documents.service.DocumentFactory;
import net.pladema.emergencycare.repository.EmergencyCareEpisodeDischargeRepository;
import net.pladema.emergencycare.repository.entity.EmergencyCareDischarge;
import net.pladema.emergencycare.service.EmergencyCareEpisodeDischargeService;
import net.pladema.emergencycare.service.domain.MedicalDischargeBo;
import org.springframework.stereotype.Service;

@Service
public class EmergencyCareEpisodeDischargeServiceImpl implements EmergencyCareEpisodeDischargeService {

    private EmergencyCareEpisodeDischargeRepository emergencyCareEpisodeDischargeRepository;
    private final DocumentFactory documentFactory;

    EmergencyCareEpisodeDischargeServiceImpl(EmergencyCareEpisodeDischargeRepository emergencyCareEpisodeDischargeRepository, DocumentFactory documentFactory) {
        this.emergencyCareEpisodeDischargeRepository = emergencyCareEpisodeDischargeRepository;

        this.documentFactory = documentFactory;
    }

    @Override
    public boolean newMedicalDischarge(MedicalDischargeBo medicalDischarge) {
        EmergencyCareDischarge newDischarge = toEmergencyCareDischarge(medicalDischarge);
        emergencyCareEpisodeDischargeRepository.save(newDischarge);
        documentFactory.run(medicalDischarge);
        return true;
    }

    private EmergencyCareDischarge toEmergencyCareDischarge(MedicalDischargeBo medicalDischarge ) {
        return new EmergencyCareDischarge(medicalDischarge.getSourceId(),medicalDischarge.getMedicalDischargeOn(),medicalDischarge.getMedicalDischargeBy(),medicalDischarge.getAutopsy(), medicalDischarge.getDischargeTypeId());
    }

}
