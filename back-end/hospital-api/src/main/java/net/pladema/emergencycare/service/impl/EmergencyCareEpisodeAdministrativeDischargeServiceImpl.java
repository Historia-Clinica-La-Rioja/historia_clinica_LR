package net.pladema.emergencycare.service.impl;

import io.jsonwebtoken.lang.Assert;
import net.pladema.emergencycare.controller.EmergencyCareEpisodeMedicalDischargeController;
import net.pladema.emergencycare.repository.EmergencyCareEpisodeDischargeRepository;
import net.pladema.emergencycare.repository.EmergencyCareEpisodeRepository;
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

@Service
public class EmergencyCareEpisodeAdministrativeDischargeServiceImpl implements EmergencyCareEpisodeAdministrativeDischargeService {

    private static final Logger LOG = LoggerFactory.getLogger(EmergencyCareEpisodeMedicalDischargeController.class);
    private final EmergencyCareEpisodeStateService emergencyCareEpisodeStateService;
    private final EmergencyCareEpisodeDischargeRepository emergencyCareEpisodeDischargeRepository;

	private final EmergencyCareEpisodeRepository emergencyCareEpisodeRepository;
    private final DateTimeProvider dateTimeProvider;

    public EmergencyCareEpisodeAdministrativeDischargeServiceImpl(EmergencyCareEpisodeStateService emergencyCareEpisodeStateService,
																  EmergencyCareEpisodeDischargeRepository emergencyCareEpisodeDischargeRepository,
																  EmergencyCareEpisodeRepository emergencyCareEpisodeRepository,
																  DateTimeProvider dateTimeProvider) {
        this.emergencyCareEpisodeStateService = emergencyCareEpisodeStateService;
        this.emergencyCareEpisodeDischargeRepository = emergencyCareEpisodeDischargeRepository;
		this.emergencyCareEpisodeRepository = emergencyCareEpisodeRepository;
        this.dateTimeProvider = dateTimeProvider;
    }

    @Override
	@Transactional
    public boolean newAdministrativeDischarge(AdministrativeDischargeBo administrativeDischargeBo, Integer institutionId){
        LOG.debug("New administrative discharge  -> administrativeDischargeBo {}, institutionId{}", administrativeDischargeBo, institutionId);
        EmergencyCareDischarge emergencyCareDischarge = emergencyCareEpisodeDischargeRepository.findById(administrativeDischargeBo.getEpisodeId()).orElse(new EmergencyCareDischarge((short) 6));
		if (emergencyCareDischarge.getEmergencyCareEpisodeId() == null)
			assertHasEvolutionNote(administrativeDischargeBo.getEpisodeId());
        emergencyCareDischarge.setAdministrativeDischargeByUser(administrativeDischargeBo.getUserId());
        emergencyCareDischarge.setAdministrativeDischargeOn(administrativeDischargeBo.getAdministrativeDischargeOn());
        emergencyCareDischarge.setEmergencyCareEpisodeId(administrativeDischargeBo.getEpisodeId());
        emergencyCareDischarge.setHospitalTransportId(administrativeDischargeBo.getHospitalTransportId());
        emergencyCareDischarge.setAmbulanceCompanyId(administrativeDischargeBo.getAmbulanceCompanyId());
		if (emergencyCareDischarge.getMedicalDischargeOn() != null)
        	assertValidDischarge(administrativeDischargeBo, emergencyCareDischarge.getMedicalDischargeOn());
        emergencyCareDischarge = emergencyCareEpisodeDischargeRepository.save(emergencyCareDischarge);
        emergencyCareEpisodeStateService.changeState(emergencyCareDischarge.getEmergencyCareEpisodeId(), institutionId, EmergencyCareState.CON_ALTA_ADMINISTRATIVA, null, null, null);
        return true;
    }

	@Override
	public boolean hasAdministrativeDischarge(Integer episodeId) {
		LOG.debug("Input parameters -> episodeId {}", episodeId);
		Boolean result = emergencyCareEpisodeDischargeRepository.findById(episodeId).map(EmergencyCareDischarge::getAdministrativeDischargeOn).isPresent();
		LOG.debug("Output -> result {}", result);
		return result;
	}

	private void assertHasEvolutionNote(Integer episodeId) {
		Assert.isTrue(!emergencyCareEpisodeRepository.episodeHasEvolutionNote(episodeId), "El episodio requiere alta médica debido a que tiene notas de evolución asociadas");
	}

    private void assertValidDischarge(AdministrativeDischargeBo administrativeDischargeBo, LocalDateTime medicalDischargeDate) {
        LocalDateTime administrativeDischargeOn = administrativeDischargeBo.getAdministrativeDischargeOn();
        Assert.isTrue( !administrativeDischargeOn.isBefore(medicalDischargeDate), "La fecha de alta administrativa debe ser posterior o igual a la fecha de alta medica del episodio");

        LocalDateTime today = dateTimeProvider.nowDateTime();
        Assert.isTrue( !administrativeDischargeOn.isAfter(today), "La fecha de alta administrativa debe ser anterior o igual a la fecha actual");
    }

}
