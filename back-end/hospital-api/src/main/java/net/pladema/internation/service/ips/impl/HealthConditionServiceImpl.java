package net.pladema.internation.service.ips.impl;

import net.pladema.internation.repository.ips.HealthConditionRepository;
import net.pladema.internation.repository.ips.entity.HealthCondition;
import net.pladema.internation.repository.ips.generalstate.HealthConditionVo;
import net.pladema.internation.repository.masterdata.entity.ConditionClinicalStatus;
import net.pladema.internation.repository.masterdata.entity.ConditionProblemType;
import net.pladema.internation.repository.masterdata.entity.ConditionVerificationStatus;
import net.pladema.internation.repository.masterdata.entity.ProblemType;
import net.pladema.internation.service.documents.DocumentService;
import net.pladema.internation.service.general.NoteService;
import net.pladema.internation.service.ips.HealthConditionService;
import net.pladema.internation.service.ips.SnomedService;
import net.pladema.internation.service.ips.domain.DiagnosisBo;
import net.pladema.internation.service.ips.domain.GeneralHealthConditionBo;
import net.pladema.internation.service.ips.domain.HealthConditionBo;
import net.pladema.internation.service.ips.domain.HealthHistoryConditionBo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class HealthConditionServiceImpl implements HealthConditionService {

    public static final String OUTPUT = "Output -> {}";

    private static final Logger LOG = LoggerFactory.getLogger(HealthConditionServiceImpl.class);

    private static final String LOGGING_HEALTH_CONDITION = "HealthCondition saved ->";
    private static final String LOGGING_INTERNMENT_EPISODE = "Input parameters -> internmentEpisodeId {}";

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
    public HealthConditionBo loadMainDiagnosis(Integer patientId, Long documentId, Optional<HealthConditionBo> mainDiagnosis) {
        LOG.debug("Input parameters -> patientId {}, documentId {}, mainDiagnosis {}", documentId, patientId, mainDiagnosis);
        mainDiagnosis.ifPresent(md -> {
            HealthCondition healthCondition = buildHealth(patientId, md, true);
            healthCondition.setMain(true);
            healthCondition = updateStatusAndVerification(healthCondition, md);
            healthCondition = healthConditionRepository.save(healthCondition);
            LOG.debug(LOGGING_HEALTH_CONDITION, healthCondition.getId());
            md.setId(healthCondition.getId());
            md.setVerificationId(healthCondition.getVerificationStatusId());
            md.setStatusId(healthCondition.getStatusId());

            documentService.createDocumentHealthCondition(documentId, healthCondition.getId());
        });
        HealthConditionBo result = mainDiagnosis.orElse(null);
        LOG.debug(OUTPUT, result);
        return result;
    }

    @Override
    public List<DiagnosisBo> loadDiagnosis(Integer patientId, Long documentId, List<DiagnosisBo> diagnosis) {
        LOG.debug("Input parameters -> patientId {}, documentId {}, diagnosis {}", documentId, patientId, diagnosis);
        diagnosis.forEach(d -> {
            HealthCondition healthCondition = buildHealth(patientId, d, true);
            if (d.isPresumptive())
                healthCondition.setVerificationStatusId(ConditionVerificationStatus.PRESUMPTIVE);
            healthCondition = updateStatusAndVerification(healthCondition, d);
            healthCondition = healthConditionRepository.save(healthCondition);
            LOG.debug(LOGGING_HEALTH_CONDITION, healthCondition.getId());

            d.setId(healthCondition.getId());
            d.setVerificationId(healthCondition.getVerificationStatusId());
            d.setStatusId(healthCondition.getStatusId());

            documentService.createDocumentHealthCondition(documentId, healthCondition.getId());
        });
        List<DiagnosisBo> result = diagnosis;
        LOG.debug(OUTPUT, result);
        return result;
    }

    private <T extends HealthConditionBo> HealthCondition updateStatusAndVerification(HealthCondition healthCondition, T newDiagnosis) {
        if (newDiagnosis.isError()) {
            healthCondition.setStatusId(ConditionClinicalStatus.INACTIVE);
            healthCondition.setVerificationStatusId(newDiagnosis.getVerificationId());
        }
        if (newDiagnosis.isDiscarded())
            healthCondition.setStatusId(newDiagnosis.getStatusId());
            healthCondition.setVerificationStatusId(newDiagnosis.getVerificationId());
        return healthCondition;
    }

    @Override
    public List<HealthHistoryConditionBo> loadPersonalHistories(Integer patientId, Long documentId, List<HealthHistoryConditionBo> personalHistories) {
        LOG.debug("Input parameters -> patientId {}, documentId {}, personalHistories {}", documentId, patientId, personalHistories);
        personalHistories.forEach(ph -> {
            HealthCondition healthCondition = buildHistoryHealth(patientId, ph, true);
            healthCondition = healthConditionRepository.save(healthCondition);
            LOG.debug(LOGGING_HEALTH_CONDITION, healthCondition.getId());

            ph.setId(healthCondition.getId());
            ph.setVerificationId(healthCondition.getVerificationStatusId());
            ph.setStatusId(healthCondition.getStatusId());

            documentService.createDocumentHealthCondition(documentId, healthCondition.getId());
        });
        List<HealthHistoryConditionBo> result = personalHistories;
        LOG.debug(OUTPUT, result);
        return result;
    }

    @Override
    public List<HealthHistoryConditionBo> loadFamilyHistories(Integer patientId, Long documentId, List<HealthHistoryConditionBo> familyHistories) {
        LOG.debug("Input parameters -> patientId {}, documentId {}, familyHistories {}", documentId, patientId, familyHistories);
        familyHistories.forEach(ph -> {
            HealthCondition healthCondition = buildHistoryHealth(patientId, ph, false);
            healthCondition = healthConditionRepository.save(healthCondition);
            LOG.debug(LOGGING_HEALTH_CONDITION, healthCondition.getId());

            ph.setId(healthCondition.getId());
            ph.setVerificationId(healthCondition.getVerificationStatusId());
            ph.setStatusId(healthCondition.getStatusId());

            documentService.createDocumentHealthCondition(documentId, healthCondition.getId());
        });
        List<HealthHistoryConditionBo> result = familyHistories;
        LOG.debug(OUTPUT, result);
        return result;
    }

    private <T extends HealthHistoryConditionBo> HealthCondition buildHistoryHealth(Integer patientId, T healthHistory, boolean personal) {
        LOG.debug("Input parameters -> patientId {}, info {}, healthHistory {}", patientId, healthHistory, personal);
        HealthCondition healthCondition = buildHealth(patientId, healthHistory, personal);
        healthCondition.setProblemId(personal ? ProblemType.PROBLEMA : ProblemType.ANTECEDENTE);

        LocalDate date = healthHistory.getDate() == null ? defaultDate() : healthHistory.getDate();
        healthCondition.setStartDate(date);

        healthCondition.setNoteId(noteService.createNote(healthHistory.getNote()));
        LOG.debug(OUTPUT, healthCondition);
        return healthCondition;
    }

    private LocalDate defaultDate() {
        return LocalDate.now();
    }

    private <T extends HealthConditionBo> HealthCondition buildHealth(Integer patientId, T info, boolean personal) {
        LOG.debug("Input parameters -> patientId {}, info {}, personal {}", patientId, info, personal);
        String sctId = snomedService.createSnomedTerm(info.getSnomed());
        HealthCondition healthCondition = new HealthCondition();
        healthCondition.setPatientId(patientId);
        healthCondition.setSctidCode(sctId);
        healthCondition.setStatusId(ConditionClinicalStatus.ACTIVE);
        healthCondition.setProblemTypeId(ConditionProblemType.PROBLEMA);
        healthCondition.setVerificationStatusId(info.getVerificationId());
        healthCondition.setPersonal(personal);
        healthCondition.setProblemId(ProblemType.DIAGNOSTICO);
        LOG.debug(OUTPUT, healthCondition);
        return healthCondition;
    }

    @Override
    public GeneralHealthConditionBo getGeneralState(Integer internmentEpisodeId) {
        LOG.debug(LOGGING_INTERNMENT_EPISODE, internmentEpisodeId);
        List<HealthConditionVo> data = getGeneralStateData(internmentEpisodeId);
        GeneralHealthConditionBo result = new GeneralHealthConditionBo(data);
        LOG.debug(OUTPUT, result);
        return result;
    }

    @Override
    public HealthConditionBo getMainDiagnosisGeneralState(Integer internmentEpisodeId) {
        LOG.debug(LOGGING_INTERNMENT_EPISODE, internmentEpisodeId);
        List<HealthConditionVo> data = getGeneralStateData(internmentEpisodeId);
        GeneralHealthConditionBo generalHealthConditionBo = new GeneralHealthConditionBo(data);
        HealthConditionBo result =  generalHealthConditionBo.getMainDiagnosis();
        LOG.debug(OUTPUT, result);
        return result;
    }

    @Override
    public List<DiagnosisBo> getDiagnosisGeneralState(Integer internmentEpisodeId) {
        LOG.debug(LOGGING_INTERNMENT_EPISODE, internmentEpisodeId);
        List<HealthConditionVo> data = getGeneralStateData(internmentEpisodeId);
        GeneralHealthConditionBo generalHealthConditionBo = new GeneralHealthConditionBo(data);
        List<DiagnosisBo> result =  generalHealthConditionBo.getDiagnosis();
        LOG.debug(OUTPUT, result);
        return result;
    }

    @Override
    public List<HealthHistoryConditionBo> getPersonalHistoriesGeneralState(Integer internmentEpisodeId) {
        LOG.debug(LOGGING_INTERNMENT_EPISODE, internmentEpisodeId);
        List<HealthConditionVo> data = getGeneralStateData(internmentEpisodeId);
        GeneralHealthConditionBo generalHealthConditionBo = new GeneralHealthConditionBo(data);
        List<HealthHistoryConditionBo> result =  generalHealthConditionBo.getPersonalHistories();
        LOG.debug(OUTPUT, result);
        return result;
    }

    @Override
    public List<HealthHistoryConditionBo> getFamilyHistoriesGeneralState(Integer internmentEpisodeId) {
        LOG.debug(LOGGING_INTERNMENT_EPISODE, internmentEpisodeId);
        List<HealthConditionVo> data = getGeneralStateData(internmentEpisodeId);
        GeneralHealthConditionBo generalHealthConditionBo = new GeneralHealthConditionBo(data);
        List<HealthHistoryConditionBo> result =  generalHealthConditionBo.getFamilyHistories();
        LOG.debug(OUTPUT, result);
        return result;
    }

    private List<HealthConditionVo> getGeneralStateData(Integer internmentEpisodeId) {
        return healthConditionRepository.findGeneralState(internmentEpisodeId);
    }


}
