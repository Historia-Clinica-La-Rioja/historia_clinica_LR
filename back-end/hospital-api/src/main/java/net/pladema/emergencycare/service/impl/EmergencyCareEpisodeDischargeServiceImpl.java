package net.pladema.emergencycare.service.impl;

import net.pladema.clinichistory.documents.service.DocumentFactory;
import net.pladema.emergencycare.repository.EmergencyCareEpisodeDischargeRepository;
import net.pladema.emergencycare.repository.entity.EmergencyCareDischarge;
import net.pladema.emergencycare.repository.entity.EmergencyCareState;
import net.pladema.emergencycare.service.EmergencyCareEpisodeDischargeService;
import net.pladema.emergencycare.service.EmergencyCareEpisodeStateService;
import net.pladema.emergencycare.service.domain.AdministrativeDischargeBo;
import net.pladema.emergencycare.service.domain.MedicalDischargeBo;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class EmergencyCareEpisodeDischargeServiceImpl implements EmergencyCareEpisodeDischargeService {

    private EmergencyCareEpisodeDischargeRepository emergencyCareEpisodeDischargeRepository;
    private final DocumentFactory documentFactory;
    private final EmergencyCareEpisodeStateService emergencyCareEpisodeStateService;

    EmergencyCareEpisodeDischargeServiceImpl(EmergencyCareEpisodeDischargeRepository emergencyCareEpisodeDischargeRepository, DocumentFactory documentFactory,
                                             EmergencyCareEpisodeStateService emergencyCareEpisodeStateService) {
        this.emergencyCareEpisodeDischargeRepository = emergencyCareEpisodeDischargeRepository;
        this.documentFactory = documentFactory;
        this.emergencyCareEpisodeStateService = emergencyCareEpisodeStateService;
    }

    @Override
    public boolean newMedicalDischarge(MedicalDischargeBo medicalDischarge) {
        EmergencyCareDischarge newDischarge = toEmergencyCareDischarge(medicalDischarge);
        emergencyCareEpisodeDischargeRepository.save(newDischarge);
        documentFactory.run(medicalDischarge);
        return true;
    }

    @Override
    public boolean newAdministrativeDischarge(AdministrativeDischargeBo administrativeDischargeBo, Integer institutionId) {
        EmergencyCareDischarge emergencyCareDischarge = emergencyCareEpisodeDischargeRepository.findById(administrativeDischargeBo.getEpisodeId()).orElse( new EmergencyCareDischarge());
        emergencyCareDischarge.setAdministrativeDischargeByUser(administrativeDischargeBo.getUserId());
        emergencyCareDischarge.setAdministrativeDischargeOn(administrativeDischargeBo.getAdministrativeDischargeOn());
        emergencyCareDischarge.setEmergencyCareEpisodeId(administrativeDischargeBo.getEpisodeId());
        emergencyCareDischarge.setHospitalTransportId(administrativeDischargeBo.getHospitalTransportId());
        emergencyCareDischarge.setAmbulanceCompanyId(administrativeDischargeBo.getAmbulanceCompanyId());
        emergencyCareDischarge = emergencyCareEpisodeDischargeRepository.save(emergencyCareDischarge);
        emergencyCareEpisodeStateService.changeState(emergencyCareDischarge.getEmergencyCareEpisodeId(), institutionId, EmergencyCareState.CON_ALTA_ADMINISTRATIVA, null);
        return true;
    }

    private EmergencyCareDischarge toEmergencyCareDischarge(MedicalDischargeBo medicalDischarge ) {
        return new EmergencyCareDischarge(medicalDischarge.getSourceId(),medicalDischarge.getMedicalDischargeOn(),medicalDischarge.getMedicalDischargeBy(),medicalDischarge.getAutopsy(), medicalDischarge.getDischargeTypeId());
    }

}
