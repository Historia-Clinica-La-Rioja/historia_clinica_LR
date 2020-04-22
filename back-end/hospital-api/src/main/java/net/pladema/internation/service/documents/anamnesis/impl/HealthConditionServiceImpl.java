package net.pladema.internation.service.documents.anamnesis.impl;

import net.pladema.internation.controller.dto.SnomedDto;
import net.pladema.internation.repository.ips.HealthConditionRepository;
import net.pladema.internation.repository.ips.entity.HealthCondition;
import net.pladema.internation.repository.masterdata.NoteRepository;
import net.pladema.internation.repository.masterdata.SnomedRepository;
import net.pladema.internation.repository.masterdata.entity.ConditionProblemType;
import net.pladema.internation.repository.masterdata.entity.Note;
import net.pladema.internation.repository.masterdata.entity.ProblemType;
import net.pladema.internation.repository.masterdata.entity.Snomed;
import net.pladema.internation.service.documents.DocumentService;
import net.pladema.internation.service.documents.anamnesis.HealthConditionService;
import net.pladema.internation.service.domain.ips.HealthConditionBo;
import net.pladema.internation.service.domain.ips.HealthHistoryCondition;
import net.pladema.patient.service.StringHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class HealthConditionServiceImpl implements HealthConditionService {

    private static final Logger LOG = LoggerFactory.getLogger(HealthConditionServiceImpl.class);

    private final HealthConditionRepository healthConditionRepository;
    private final NoteRepository noteRepository;
    private final SnomedRepository snomedRepository;
    private final DocumentService documentService;

    public HealthConditionServiceImpl(HealthConditionRepository healthConditionRepository,
                                      NoteRepository noteRepository,
                                      SnomedRepository snomedRepository,
                                      DocumentService documentService){
        this.healthConditionRepository = healthConditionRepository;
        this.noteRepository = noteRepository;
        this.snomedRepository = snomedRepository;
        this.documentService = documentService;
    }

    @Override
    public void loadDiagnosis(Integer patientId, Long documentId, List<HealthConditionBo> diagnosis) throws DataIntegrityViolationException {
        LOG.debug("Going to load diagnosis -> {}", diagnosis);
        if(diagnosis.isEmpty())
            throw new IllegalArgumentException("diagnosis.mandatory");
        diagnosis.forEach(d -> {
            HealthCondition healthCondition = buildHealth(patientId, d, true);
            healthCondition = healthConditionRepository.save(healthCondition);

            documentService.createHealthConditionIndex(documentId, healthCondition.getId());
        });
    }

    @Override
    public void loadPersonalHistories(Integer patientId, Long documentId, List<HealthHistoryCondition> personalHistories) throws DataIntegrityViolationException {
        LOG.debug("Going to load Personal Histories -> {}", personalHistories);
        personalHistories.forEach(ph -> {
            HealthCondition healthCondition = buildHistoryHealth(patientId, ph, true);
            healthConditionRepository.save(healthCondition);

            documentService.createHealthConditionIndex(documentId, healthCondition.getId());
        });
    }

    @Override
    public void loadFamilyHistories(Integer patientId, Long documentId, List<HealthHistoryCondition> familyHistories) throws DataIntegrityViolationException {
        LOG.debug("Going to load Family Histories -> {}", familyHistories);
        familyHistories.forEach(ph -> {
            HealthCondition healthCondition = buildHistoryHealth(patientId, ph, false);
            healthConditionRepository.save(healthCondition);

            documentService.createHealthConditionIndex(documentId, healthCondition.getId());
        });
    }

    private <T extends HealthConditionBo> HealthCondition buildHealth(Integer patientId, T info, boolean personal) throws NullPointerException{

        saveSnomedTerm(info.getSnomed());

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
        healthCondition.setStartDate(LocalDate.parse(healthHistory.getDate()));
        healthCondition.setNoteId(saveNote(healthHistory.getNote()));
        return healthCondition;
    }

    private void saveSnomedTerm(SnomedDto snomedTerm){
        if(!StringHelper.isNullOrWhiteSpace(snomedTerm.getId()) && !StringHelper.isNullOrWhiteSpace(snomedTerm.getFsn()))
            snomedRepository.save(new Snomed(
                    snomedTerm.getId(),
                    snomedTerm.getFsn(),
                    snomedTerm.getId(),
                    snomedTerm.getFsn())
            );
    }

    private Long saveNote(String description){
        if(!StringHelper.isNullOrWhiteSpace(description)) {
            Note note = new Note();
            note.setDescription(description);
            note = noteRepository.save(note);
            return note.getId();
        }
        return -1L;
    }
}
