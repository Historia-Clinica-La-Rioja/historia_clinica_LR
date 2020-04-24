package net.pladema.internation.service.documents.anamnesis.impl;

import net.pladema.internation.repository.ips.HealthConditionRepository;
import net.pladema.internation.repository.ips.entity.HealthCondition;
import net.pladema.internation.repository.masterdata.entity.ConditionProblemType;
import net.pladema.internation.repository.masterdata.entity.ProblemType;
import net.pladema.internation.service.NoteService;
import net.pladema.internation.service.SnomedService;
import net.pladema.internation.service.documents.DocumentService;
import net.pladema.internation.service.documents.anamnesis.HealthConditionService;
import net.pladema.internation.service.domain.ips.HealthConditionBo;
import net.pladema.internation.service.domain.ips.HealthHistoryCondition;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.List;

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
    public void loadDiagnosis(Integer patientId, Long documentId, List<HealthConditionBo> diagnosis) throws DataIntegrityViolationException {
        LOG.debug("Going to load diagnosis -> {}", diagnosis);
        if(diagnosis.isEmpty())
            throw new IllegalArgumentException("diagnosis.mandatory");
        diagnosis.forEach(d -> {
            HealthCondition healthCondition = buildHealth(patientId, d, true);
            healthCondition = healthConditionRepository.save(healthCondition);

            documentService.createDocumentHealthCondition(documentId, healthCondition.getId());
        });
    }

    @Override
    public void loadPersonalHistories(Integer patientId, Long documentId, List<HealthHistoryCondition> personalHistories) throws DataIntegrityViolationException {
        LOG.debug("Going to load Personal Histories -> {}", personalHistories);
        personalHistories.forEach(ph -> {
            HealthCondition healthCondition = buildHistoryHealth(patientId, ph, true);
            healthCondition = healthConditionRepository.save(healthCondition);

            documentService.createDocumentHealthCondition(documentId, healthCondition.getId());
        });
    }

    @Override
    public void loadFamilyHistories(Integer patientId, Long documentId, List<HealthHistoryCondition> familyHistories) throws DataIntegrityViolationException {
        LOG.debug("Going to load Family Histories -> {}", familyHistories);
        familyHistories.forEach(ph -> {
            HealthCondition healthCondition = buildHistoryHealth(patientId, ph, false);
            healthCondition = healthConditionRepository.save(healthCondition);

            documentService.createDocumentHealthCondition(documentId, healthCondition.getId());
        });
    }

    private <T extends HealthConditionBo> HealthCondition buildHealth(Integer patientId, T info, boolean personal) throws NullPointerException{

        String sctid = snomedService.createSnomedTerm(info.getSnomed());
        if(sctid == null)
            throw new IllegalArgumentException("snomed.invalid");

        HealthCondition healthCondition = new HealthCondition();
        healthCondition.setPatientId(patientId);
        healthCondition.setSctidCode(info.getSnomed().getId());
        healthCondition.setStatusId(info.getStatusId());
        healthCondition.setVerificationStatusId(info.getVerificationId());
        healthCondition.setProblemTypeId(ConditionProblemType.PROBLEMA);
        healthCondition.setPersonal(personal);
        healthCondition.setProblemId(ProblemType.DIAGNOSTICO);
        return healthCondition;
    }

    private <T extends HealthHistoryCondition> HealthCondition buildHistoryHealth(Integer patientId, T healthHistory, boolean personal) throws NullPointerException{
        HealthCondition healthCondition = buildHealth(patientId, healthHistory, personal);
        healthCondition.setProblemId(personal ? ProblemType.PROBLEMA : ProblemType.ANTECEDENTE);
        healthCondition.setStartDate(healthHistory.getDate());
        healthCondition.setNoteId(noteService.createNote(healthHistory.getNote()));
        return healthCondition;
    }
}
