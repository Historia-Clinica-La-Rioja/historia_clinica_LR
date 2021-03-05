package net.pladema.emergencycare.service.impl;

import net.pladema.clinichistory.hospitalization.repository.domain.DischargeType;
import net.pladema.emergencycare.controller.EmergencyCareEpisodeMedicalDischargeController;
import net.pladema.emergencycare.repository.EmergencyCareEpisodeDischargeRepository;
import net.pladema.emergencycare.repository.entity.EmergencyCareDischarge;
import net.pladema.emergencycare.repository.entity.EmergencyCareState;
import net.pladema.emergencycare.service.EmergencyCareEpisodeAdministrativeDischargeService;
import net.pladema.emergencycare.service.EmergencyCareEpisodeStateService;
import net.pladema.emergencycare.service.domain.AdministrativeDischargeBo;
import net.pladema.sgx.dates.configuration.JacksonDateFormatConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;

import static net.pladema.emergencycare.repository.entity.EmergencyCareDischarge.WITHOUT_DOCTOR;

@Service
public class EmergencyCareEpisodeAdministrativeDischargeServiceImpl implements EmergencyCareEpisodeAdministrativeDischargeService {

    private static final Logger LOG = LoggerFactory.getLogger(EmergencyCareEpisodeMedicalDischargeController.class);
    private final EmergencyCareEpisodeStateService emergencyCareEpisodeStateService;
    private final EmergencyCareEpisodeDischargeRepository emergencyCareEpisodeDischargeRepository;

    public EmergencyCareEpisodeAdministrativeDischargeServiceImpl(EmergencyCareEpisodeStateService emergencyCareEpisodeStateService, EmergencyCareEpisodeDischargeRepository emergencyCareEpisodeDischargeRepository) {
        this.emergencyCareEpisodeStateService = emergencyCareEpisodeStateService;
        this.emergencyCareEpisodeDischargeRepository = emergencyCareEpisodeDischargeRepository;
    }

    @Override
    public boolean newAdministrativeDischarge(AdministrativeDischargeBo administrativeDischargeBo, Integer institutionId) {
        LOG.debug("New administrative discharge  -> administrativeDischargeBo {}, institutionId{}", administrativeDischargeBo, institutionId);
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

    @Override
    public boolean newAdministrativeDischargeByAbsence(Integer episodeId, Integer institutionId, Integer userId, ZoneId institutionZoneId) {
        LOG.debug("New administrative discharge by absence  -> episodeId {}, institutionId{}, userId{}, institutionZoneId{}", episodeId, institutionId, userId, institutionZoneId);
        LocalDateTime localDateTIme = LocalDateTime.now()
                .atZone(ZoneId.of(JacksonDateFormatConfig.UTC_ZONE_ID))
                .withZoneSameInstant(institutionZoneId)
                .toLocalDateTime();
        EmergencyCareDischarge emergencyCareDischarge = new EmergencyCareDischarge(episodeId,localDateTIme,userId, DischargeType.RETIRO_VOLUNTARIO, WITHOUT_DOCTOR);
        emergencyCareDischarge = emergencyCareEpisodeDischargeRepository.save(emergencyCareDischarge);
        emergencyCareEpisodeStateService.changeState(emergencyCareDischarge.getEmergencyCareEpisodeId(), institutionId, EmergencyCareState.CON_ALTA_ADMINISTRATIVA, null);
        return true;
    }
}
