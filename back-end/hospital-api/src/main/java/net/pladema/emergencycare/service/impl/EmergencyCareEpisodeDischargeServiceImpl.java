package net.pladema.emergencycare.service.impl;

import net.pladema.clinichistory.documents.repository.DocumentHealthConditionRepository;
import net.pladema.clinichistory.documents.repository.generalstate.domain.HealthConditionVo;
import net.pladema.clinichistory.documents.service.DocumentFactory;
import net.pladema.clinichistory.documents.service.DocumentService;
import net.pladema.clinichistory.documents.service.ips.domain.HealthHistoryConditionBo;
import net.pladema.clinichistory.documents.service.ips.domain.SnomedBo;
import net.pladema.clinichistory.hospitalization.repository.domain.DischargeType;
import net.pladema.clinichistory.outpatient.repository.domain.SourceType;
import net.pladema.emergencycare.controller.EmergencyCareEpisodeMedicalDischargeController;
import net.pladema.emergencycare.repository.DischargeTypeRepository;
import net.pladema.emergencycare.repository.EmergencyCareEpisodeDischargeRepository;
import net.pladema.emergencycare.repository.entity.EmergencyCareDischarge;
import net.pladema.emergencycare.service.EmergencyCareEpisodeDischargeService;
import net.pladema.emergencycare.service.domain.EpisodeDischargeBo;
import net.pladema.emergencycare.service.domain.MedicalDischargeBo;
import net.pladema.sgx.exceptions.NotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class EmergencyCareEpisodeDischargeServiceImpl implements EmergencyCareEpisodeDischargeService {

    private static final Logger LOG = LoggerFactory.getLogger(EmergencyCareEpisodeMedicalDischargeController.class);

    private final EmergencyCareEpisodeDischargeRepository emergencyCareEpisodeDischargeRepository;
    private final DocumentFactory documentFactory;
    private final DischargeTypeRepository dischargeTypeRepository;
    private final DocumentService documentService;
    private final DocumentHealthConditionRepository documentHealthConditionRepository;

    EmergencyCareEpisodeDischargeServiceImpl(EmergencyCareEpisodeDischargeRepository emergencyCareEpisodeDischargeRepository, DocumentFactory documentFactory,
                                             DischargeTypeRepository dischargeTypeRepository, DocumentService documentService, DocumentHealthConditionRepository documentHealthConditionRepository) {
        this.emergencyCareEpisodeDischargeRepository = emergencyCareEpisodeDischargeRepository;
        this.documentFactory = documentFactory;
        this.dischargeTypeRepository = dischargeTypeRepository;
        this.documentService = documentService;
        this.documentHealthConditionRepository = documentHealthConditionRepository;
    }

    @Override
    public boolean newMedicalDischarge(MedicalDischargeBo medicalDischarge) {
        LOG.debug("Medical discharge service -> medicalDischargeBo {}", medicalDischarge);
        EmergencyCareDischarge newDischarge = toEmergencyCareDischarge(medicalDischarge);
        emergencyCareEpisodeDischargeRepository.save(newDischarge);
        documentFactory.run(medicalDischarge);
        return true;
    }

    @Override
    public EpisodeDischargeBo getDischarge(Integer episodeId) {
        LOG.debug("Get discharge -> episodeId {}", episodeId);
        EmergencyCareDischarge emergencyCareDischarge = emergencyCareEpisodeDischargeRepository.findById(episodeId)
                .orElseThrow(()->new NotFoundException("episode-discharge-not-found", "Episode discharge not found"));
        DischargeType dischargeType = dischargeTypeRepository.findById(emergencyCareDischarge.getDischargeTypeId())
                .orElseThrow(()->new NotFoundException("discharge-type-not-found", "Discharge type not found"));
        EpisodeDischargeBo episodeDischargeBo = new EpisodeDischargeBo(emergencyCareDischarge, dischargeType);
        Long documentId = documentService.getDocumentId(emergencyCareDischarge.getEmergencyCareEpisodeId(), SourceType.EMERGENCY_CARE).get(0);
        List<HealthConditionVo> resultQuery = documentHealthConditionRepository.getHealthConditionFromDocument(documentId);
        List<SnomedBo> problems = resultQuery.stream().map( r -> new SnomedBo(r.getSnomed())).collect(Collectors.toList());
        episodeDischargeBo.setProblems(problems);
        LOG.debug("output -> episodeDischargeBo {}", episodeDischargeBo);
        return episodeDischargeBo;
    }

    private EmergencyCareDischarge toEmergencyCareDischarge(MedicalDischargeBo medicalDischarge ) {
        return new EmergencyCareDischarge(medicalDischarge.getSourceId(),medicalDischarge.getMedicalDischargeOn(),medicalDischarge.getMedicalDischargeBy(),medicalDischarge.getAutopsy(), medicalDischarge.getDischargeTypeId());
    }

}