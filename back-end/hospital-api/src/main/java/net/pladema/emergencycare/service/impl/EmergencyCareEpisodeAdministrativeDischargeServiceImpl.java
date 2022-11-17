package net.pladema.emergencycare.service.impl;

import io.jsonwebtoken.lang.Assert;
import net.pladema.clinichistory.hospitalization.repository.domain.DischargeType;
import net.pladema.emergencycare.controller.EmergencyCareEpisodeMedicalDischargeController;
import net.pladema.emergencycare.repository.EmergencyCareEpisodeDischargeRepository;
import net.pladema.emergencycare.repository.entity.EmergencyCareDischarge;
import net.pladema.emergencycare.repository.entity.EmergencyCareState;
import net.pladema.emergencycare.service.EmergencyCareEpisodeAdministrativeDischargeService;
import net.pladema.emergencycare.service.EmergencyCareEpisodeStateService;
import net.pladema.emergencycare.service.domain.AdministrativeDischargeBo;
import ar.lamansys.sgx.shared.dates.configuration.DateTimeProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.time.LocalDateTime;
import java.time.ZoneId;

import static net.pladema.emergencycare.repository.entity.EmergencyCareDischarge.WITHOUT_DOCTOR;

@Service
public class EmergencyCareEpisodeAdministrativeDischargeServiceImpl implements EmergencyCareEpisodeAdministrativeDischargeService {

    private static final Logger LOG = LoggerFactory.getLogger(EmergencyCareEpisodeMedicalDischargeController.class);
    private final EmergencyCareEpisodeStateService emergencyCareEpisodeStateService;
    private final EmergencyCareEpisodeDischargeRepository emergencyCareEpisodeDischargeRepository;
    private final DateTimeProvider dateTimeProvider;

    public EmergencyCareEpisodeAdministrativeDischargeServiceImpl(EmergencyCareEpisodeStateService emergencyCareEpisodeStateService, EmergencyCareEpisodeDischargeRepository emergencyCareEpisodeDischargeRepository, DateTimeProvider dateTimeProvider) {
        this.emergencyCareEpisodeStateService = emergencyCareEpisodeStateService;
        this.emergencyCareEpisodeDischargeRepository = emergencyCareEpisodeDischargeRepository;
        this.dateTimeProvider = dateTimeProvider;
    }

    @Override
	@Transactional
    public boolean newAdministrativeDischarge(AdministrativeDischargeBo administrativeDischargeBo, Integer institutionId, ZoneId institutionZoneId){
        LOG.debug("New administrative discharge  -> administrativeDischargeBo {}, institutionId{}", administrativeDischargeBo, institutionId);
        EmergencyCareDischarge emergencyCareDischarge = emergencyCareEpisodeDischargeRepository.findById(administrativeDischargeBo.getEpisodeId())
                .orElseThrow(() -> new IllegalArgumentException("care-episode.administrative-discharge.after-medical-discharge"));
        emergencyCareDischarge.setAdministrativeDischargeByUser(administrativeDischargeBo.getUserId());
        emergencyCareDischarge.setAdministrativeDischargeOn(administrativeDischargeBo.getAdministrativeDischargeOn());
        emergencyCareDischarge.setEmergencyCareEpisodeId(administrativeDischargeBo.getEpisodeId());
        emergencyCareDischarge.setHospitalTransportId(administrativeDischargeBo.getHospitalTransportId());
        emergencyCareDischarge.setAmbulanceCompanyId(administrativeDischargeBo.getAmbulanceCompanyId());
        assertValidDischarge(administrativeDischargeBo, emergencyCareDischarge.getMedicalDischargeOn(), institutionZoneId);
        emergencyCareDischarge = emergencyCareEpisodeDischargeRepository.save(emergencyCareDischarge);
        emergencyCareEpisodeStateService.changeState(emergencyCareDischarge.getEmergencyCareEpisodeId(), institutionId, EmergencyCareState.CON_ALTA_ADMINISTRATIVA, null);
        return true;
    }

    @Override
	@Transactional
    public boolean newAdministrativeDischargeByAbsence(Integer episodeId, Integer institutionId, Integer userId, ZoneId institutionZoneId) {
        LOG.debug("New administrative discharge by absence  -> episodeId {}, institutionId{}, userId{}, institutionZoneId{}", episodeId, institutionId, userId, institutionZoneId);
        LocalDateTime localDateTIme = dateTimeProvider.nowDateTimeWithZone(institutionZoneId);
        EmergencyCareDischarge emergencyCareDischarge = new EmergencyCareDischarge(episodeId,localDateTIme,userId, DischargeType.RETIRO_VOLUNTARIO, WITHOUT_DOCTOR);
        emergencyCareDischarge = emergencyCareEpisodeDischargeRepository.save(emergencyCareDischarge);
        emergencyCareEpisodeStateService.changeState(emergencyCareDischarge.getEmergencyCareEpisodeId(), institutionId, EmergencyCareState.CON_ALTA_ADMINISTRATIVA, null);
        return true;
    }

    private void assertValidDischarge(AdministrativeDischargeBo administrativeDischargeBo, LocalDateTime medicalDischargeDate, ZoneId institutionZoneId) {
        LocalDateTime administrativeDischargeOn = administrativeDischargeBo.getAdministrativeDischargeOn();
        Assert.isTrue( !administrativeDischargeOn.isBefore(medicalDischargeDate), "care-episode.administrative-discharge.exceeds-min-date");

        LocalDateTime today = dateTimeProvider.nowDateTimeWithZone(institutionZoneId);
        Assert.isTrue( !administrativeDischargeOn.isAfter(today), "care-episode.administrative-discharge.exceeds-max-date");

    }

}
