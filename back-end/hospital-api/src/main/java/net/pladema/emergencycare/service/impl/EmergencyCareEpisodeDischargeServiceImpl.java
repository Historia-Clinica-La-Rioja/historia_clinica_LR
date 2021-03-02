package net.pladema.emergencycare.service.impl;

import net.pladema.clinichistory.documents.service.DocumentFactory;
import net.pladema.clinichistory.documents.service.DocumentService;
import net.pladema.clinichistory.documents.service.ips.domain.GeneralHealthConditionBo;
import net.pladema.clinichistory.hospitalization.repository.domain.DischargeType;
import net.pladema.clinichistory.outpatient.repository.domain.SourceType;
import net.pladema.emergencycare.repository.DischargeTypeRepository;
import net.pladema.emergencycare.repository.EmergencyCareEpisodeDischargeRepository;
import net.pladema.emergencycare.repository.entity.EmergencyCareDischarge;
import net.pladema.emergencycare.repository.entity.EmergencyCareState;
import net.pladema.emergencycare.service.EmergencyCareEpisodeDischargeService;
import net.pladema.emergencycare.service.EmergencyCareEpisodeStateService;
import net.pladema.emergencycare.service.domain.AdministrativeDischargeBo;
import net.pladema.emergencycare.service.domain.EpisodeDischargeBo;
import net.pladema.emergencycare.service.domain.MedicalDischargeBo;
import net.pladema.sgx.dates.configuration.JacksonDateFormatConfig;
import net.pladema.sgx.exceptions.NotFoundException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;

import static net.pladema.emergencycare.repository.entity.EmergencyCareDischarge.WITHOUT_DOCTOR;

@Service
public class EmergencyCareEpisodeDischargeServiceImpl implements EmergencyCareEpisodeDischargeService {

    private EmergencyCareEpisodeDischargeRepository emergencyCareEpisodeDischargeRepository;
    private final DocumentFactory documentFactory;
    private final EmergencyCareEpisodeStateService emergencyCareEpisodeStateService;
    private final DischargeTypeRepository dischargeTypeRepository;
    private final DocumentService documentService;

    EmergencyCareEpisodeDischargeServiceImpl(EmergencyCareEpisodeDischargeRepository emergencyCareEpisodeDischargeRepository, DocumentFactory documentFactory,
                                             EmergencyCareEpisodeStateService emergencyCareEpisodeStateService, DischargeTypeRepository dischargeTypeRepository, DocumentService documentService) {
        this.emergencyCareEpisodeDischargeRepository = emergencyCareEpisodeDischargeRepository;
        this.documentFactory = documentFactory;
        this.emergencyCareEpisodeStateService = emergencyCareEpisodeStateService;
        this.dischargeTypeRepository = dischargeTypeRepository;
        this.documentService = documentService;
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

    @Override
    public boolean newAdministrativeDischargeByAbsence(Integer episodeId, Integer institutionId, Integer userId, ZoneId institutionZoneId) {
        LocalDateTime localDateTIme = LocalDateTime.now()
                .atZone(ZoneId.of(JacksonDateFormatConfig.UTC_ZONE_ID))
                .withZoneSameInstant(institutionZoneId)
                .toLocalDateTime();
        EmergencyCareDischarge emergencyCareDischarge = new EmergencyCareDischarge(episodeId,localDateTIme,userId,DischargeType.RETIRO_VOLUNTARIO, WITHOUT_DOCTOR);
        emergencyCareDischarge = emergencyCareEpisodeDischargeRepository.save(emergencyCareDischarge);
        emergencyCareEpisodeStateService.changeState(emergencyCareDischarge.getEmergencyCareEpisodeId(), institutionId, EmergencyCareState.CON_ALTA_ADMINISTRATIVA, null);
        return true;
    }

    @Override
    public EpisodeDischargeBo getDischarge(Integer episodeId) {
        EmergencyCareDischarge emergencyCareDischarge = emergencyCareEpisodeDischargeRepository.findById(episodeId)
                .orElseThrow(()->new NotFoundException("episode-discharge-not-found", "Episode discharge not found"));
        DischargeType dischargeType = dischargeTypeRepository.findById(emergencyCareDischarge.getDischargeTypeId())
                .orElseThrow(()->new NotFoundException("discharge-type-not-found", "Discharge type not found"));
        EpisodeDischargeBo episodeDischargeBo = new EpisodeDischargeBo(emergencyCareDischarge, dischargeType);
        Long documentId = documentService.getDocumentId(emergencyCareDischarge.getEmergencyCareEpisodeId(), SourceType.EMERGENCY_CARE);
        GeneralHealthConditionBo generalHealthConditionBo = documentService.getHealthConditionFromDocument(documentId);
        episodeDischargeBo.setProblems(generalHealthConditionBo.getPersonalHistories());
        return episodeDischargeBo;
    }

    private EmergencyCareDischarge toEmergencyCareDischarge(MedicalDischargeBo medicalDischarge ) {
        return new EmergencyCareDischarge(medicalDischarge.getSourceId(),medicalDischarge.getMedicalDischargeOn(),medicalDischarge.getMedicalDischargeBy(),medicalDischarge.getAutopsy(), medicalDischarge.getDischargeTypeId());
    }

}
