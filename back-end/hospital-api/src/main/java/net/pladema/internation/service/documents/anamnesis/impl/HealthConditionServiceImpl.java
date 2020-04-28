package net.pladema.internation.service.documents.anamnesis.impl;

import net.pladema.internation.repository.ips.HealthConditionRepository;
import net.pladema.internation.repository.ips.entity.HealthCondition;
import net.pladema.internation.repository.masterdata.entity.ConditionClinicalStatus;
import net.pladema.internation.repository.masterdata.entity.ConditionProblemType;
import net.pladema.internation.repository.masterdata.entity.ConditionVerificationStatus;
import net.pladema.internation.repository.masterdata.entity.ProblemType;
import net.pladema.internation.service.NoteService;
import net.pladema.internation.service.SnomedService;
import net.pladema.internation.service.documents.DocumentService;
import net.pladema.internation.service.documents.anamnesis.HealthConditionService;
import net.pladema.internation.service.domain.ips.HealthConditionBo;
import net.pladema.internation.service.domain.ips.HealthHistoryCondition;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class HealthConditionServiceImpl implements HealthConditionService {

    private static final Logger LOG = LoggerFactory.getLogger(HealthConditionServiceImpl.class);

    private final HealthConditionRepository healthConditionRepository;
    private final SnomedService snomedService;
    private final DocumentService documentService;
    private final NoteService noteService;

    public HealthConditionServiceImpl(HealthConditionRepository healthConditionRepository,
                                      SnomedService snomedService,
                                      DocumentService documentService,
                                      NoteService noteService){
        this.healthConditionRepository = healthConditionRepository;
        this.snomedService = snomedService;
        this.documentService = documentService;
        this.noteService = noteService;
    }

    @Override
    public List<HealthConditionBo> loadDiagnosis(Integer patientId, Long documentId, List<HealthConditionBo> diagnosis) {
        LOG.debug("Input parameters -> patientId {}, documentId {}, diagnosis {}", documentId, patientId, diagnosis);
        if(diagnosis.isEmpty())
            throw new IllegalArgumentException("diagnosis.mandatory");
        diagnosis.stream().filter(d -> d.mustSave()).forEach(d -> {
            HealthCondition healthCondition = buildHealth(patientId, d, true);
            healthCondition = healthConditionRepository.save(healthCondition);
            LOG.debug("HealthCondition saved ->", healthCondition.getId());
            d.setId(healthCondition.getId());
            documentService.createDocumentHealthCondition(documentId, healthCondition.getId());
        });
        return diagnosis.stream().filter(d -> !d.isDeleted()).collect(Collectors.toList());
    }

    @Override
    public List<HealthHistoryCondition> loadPersonalHistories(Integer patientId, Long documentId, List<HealthHistoryCondition> personalHistories) {
        LOG.debug("Input parameters -> patientId {}, documentId {}, personalHistories {}", documentId, patientId, personalHistories);
        personalHistories.stream().filter(ph -> ph.mustSave()).forEach(ph -> {
            HealthCondition healthCondition = buildHistoryHealth(patientId, ph, true);
            healthCondition = healthConditionRepository.save(healthCondition);
            LOG.debug("HealthCondition saved ->", healthCondition.getId());
            ph.setId(healthCondition.getId());
            documentService.createDocumentHealthCondition(documentId, healthCondition.getId());
        });
        return personalHistories.stream().filter(ph -> !ph.isDeleted()).collect(Collectors.toList());
    }

    @Override
    public List<HealthHistoryCondition> loadFamilyHistories(Integer patientId, Long documentId, List<HealthHistoryCondition> familyHistories) {
        LOG.debug("Input parameters -> patientId {}, documentId {}, familyHistories {}", documentId, patientId, familyHistories);
        familyHistories.stream().filter(ph -> ph.mustSave()).forEach(ph -> {
            HealthCondition healthCondition = buildHistoryHealth(patientId, ph, false);
            healthCondition = healthConditionRepository.save(healthCondition);
            LOG.debug("HealthCondition saved ->", healthCondition.getId());
            ph.setId(healthCondition.getId());
            documentService.createDocumentHealthCondition(documentId, healthCondition.getId());
        });
        return familyHistories.stream().filter(fh -> !fh.isDeleted()).collect(Collectors.toList());
    }

    @Override
    public List<HealthConditionBo> getDiagnosisGeneralState(Integer internmentEpisodeId) {
        return new ArrayList<>();
    }

    @Override
    public List<HealthHistoryCondition> getPersonalHistoriesGeneralState(Integer internmentEpisodeId) {
        return new ArrayList<>();
    }

    @Override
    public List<HealthHistoryCondition> getFamilyHistoriesGeneralState(Integer internmentEpisodeId) {
        return new ArrayList<>();
    }

    private <T extends HealthHistoryCondition> HealthCondition buildHistoryHealth(Integer patientId, T healthHistory, boolean personal) {
        HealthCondition healthCondition = buildHealth(patientId, healthHistory, personal);
        healthCondition.setProblemId(personal ? ProblemType.PROBLEMA : ProblemType.ANTECEDENTE);
        healthCondition.setStartDate(healthHistory.getDate());
        healthCondition.setNoteId(noteService.createNote(healthHistory.getNote()));
        return healthCondition;
    }

    private <T extends HealthConditionBo> HealthCondition buildHealth(Integer patientId, T info, boolean personal) {
        String sctid = snomedService.createSnomedTerm(info.getSnomed());
        HealthCondition healthCondition = new HealthCondition();
        healthCondition.setPatientId(patientId);
        healthCondition.setSctidCode(sctid);
        healthCondition.setStatusId(ConditionClinicalStatus.ACTIVE);
        healthCondition.setVerificationStatusId(ConditionVerificationStatus.CONFIRMED);
        if (info.isDeleted())
            healthCondition.setVerificationStatusId(ConditionVerificationStatus.ERROR);
        healthCondition.setProblemTypeId(ConditionProblemType.PROBLEMA);
        healthCondition.setPersonal(personal);
        healthCondition.setProblemId(ProblemType.DIAGNOSTICO);
        return healthCondition;
    }

}
