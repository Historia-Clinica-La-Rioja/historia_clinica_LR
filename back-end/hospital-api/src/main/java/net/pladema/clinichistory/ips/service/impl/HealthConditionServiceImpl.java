package net.pladema.clinichistory.ips.service.impl;

import net.pladema.clinichistory.documents.service.DocumentService;
import net.pladema.clinichistory.documents.service.NoteService;
import net.pladema.clinichistory.ips.repository.HealthConditionRepository;
import net.pladema.clinichistory.ips.repository.entity.HealthCondition;
import net.pladema.clinichistory.ips.repository.masterdata.ConditionClinicalStatusRepository;
import net.pladema.clinichistory.ips.repository.masterdata.ConditionVerificationStatusRepository;
import net.pladema.clinichistory.ips.repository.masterdata.entity.ConditionClinicalStatus;
import net.pladema.clinichistory.ips.repository.masterdata.entity.ConditionVerificationStatus;
import net.pladema.clinichistory.ips.repository.masterdata.entity.ProblemType;
import net.pladema.clinichistory.ips.service.HealthConditionService;
import net.pladema.clinichistory.ips.service.SnomedService;
import net.pladema.clinichistory.ips.service.domain.DiagnosisBo;
import net.pladema.clinichistory.ips.service.domain.HealthConditionBo;
import net.pladema.clinichistory.ips.service.domain.HealthHistoryConditionBo;
import net.pladema.clinichistory.outpatient.createoutpatient.service.domain.ProblemBo;
import net.pladema.sgx.dates.configuration.DateTimeProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class HealthConditionServiceImpl implements HealthConditionService {

    public static final String OUTPUT = "Output -> {}";

    private static final Logger LOG = LoggerFactory.getLogger(HealthConditionServiceImpl.class);
    public static final String INPUT_PARAMETERS_PATIENT_ID_INFO = "Input parameters -> patientId {}, info {}";

    private final HealthConditionRepository healthConditionRepository;

    private final ConditionVerificationStatusRepository conditionVerificationStatusRepository;

    private final ConditionClinicalStatusRepository conditionClinicalStatusRepository;

    private final SnomedService snomedService;

    private final DocumentService documentService;

    private final NoteService noteService;

    private final DateTimeProvider dateTimeProvider;

    public HealthConditionServiceImpl(HealthConditionRepository healthConditionRepository,
                                      ConditionVerificationStatusRepository conditionVerificationStatusRepository,
                                      ConditionClinicalStatusRepository conditionClinicalStatusRepository, SnomedService snomedService,
                                      DocumentService documentService,
                                      NoteService noteService, DateTimeProvider dateTimeProvider){
        this.healthConditionRepository = healthConditionRepository;
        this.conditionVerificationStatusRepository = conditionVerificationStatusRepository;
        this.conditionClinicalStatusRepository = conditionClinicalStatusRepository;
        this.snomedService = snomedService;
        this.documentService = documentService;
        this.noteService = noteService;
        this.dateTimeProvider = dateTimeProvider;
    }

    private HealthCondition save(HealthCondition healthCondition){
        LOG.debug("Input parameters -> healthCondition {}", healthCondition);
        healthCondition = healthConditionRepository.save(healthCondition);
        LOG.debug(OUTPUT, healthCondition);
        return healthCondition;
    }

    @Override
    public HealthConditionBo loadMainDiagnosis(Integer patientId, Long documentId, Optional<HealthConditionBo> mainDiagnosis) {
        LOG.debug("Input parameters -> patientId {}, documentId {}, mainDiagnosis {}", documentId, patientId, mainDiagnosis);
        mainDiagnosis.ifPresent(md -> {
            HealthCondition healthCondition = buildMainDiagnoses(patientId, md);
            healthCondition = save(healthCondition);

            md.setId(healthCondition.getId());
            md.setVerificationId(healthCondition.getVerificationStatusId());
            md.setStatusId(healthCondition.getStatusId());
            md.setVerification(getVerification(md.getVerificationId()));
            md.setStatus(getStatus(md.getStatusId()));

            documentService.createDocumentHealthCondition(documentId, healthCondition.getId());
        });
        HealthConditionBo result = mainDiagnosis.orElse(null);
        LOG.debug(OUTPUT, result);
        return result;
    }

    private HealthCondition buildMainDiagnoses(Integer patientId, HealthConditionBo info) {
        LOG.debug(INPUT_PARAMETERS_PATIENT_ID_INFO, patientId, info);
        HealthCondition healthCondition = buildBasicHealthCondition(patientId, info);
        healthCondition.setProblemId(ProblemType.DIAGNOSTICO);
        healthCondition.setMain(true);
        updateStatusAndVerification(healthCondition, info);
        LOG.debug(OUTPUT, healthCondition);
        return healthCondition;
    }

    @Override
    public List<DiagnosisBo> loadDiagnosis(Integer patientId, Long documentId, List<DiagnosisBo> diagnosis) {
        LOG.debug("Input parameters -> patientId {}, documentId {}, diagnosis {}", documentId, patientId, diagnosis);
        diagnosis.forEach(d -> {
            HealthCondition healthCondition = buildDiagnoses(patientId, d);
            healthCondition = save(healthCondition);

            d.setId(healthCondition.getId());
            d.setVerificationId(healthCondition.getVerificationStatusId());
            d.setStatusId(healthCondition.getStatusId());
            d.setVerification(getVerification(d.getVerificationId()));
            d.setStatus(getStatus(d.getStatusId()));

            documentService.createDocumentHealthCondition(documentId, healthCondition.getId());
        });
        List<DiagnosisBo> result = diagnosis;
        LOG.debug(OUTPUT, result);
        return result;
    }

    private HealthCondition buildDiagnoses(Integer patientId, DiagnosisBo info) {
        LOG.debug(INPUT_PARAMETERS_PATIENT_ID_INFO, patientId, info);
        HealthCondition healthCondition = buildBasicHealthCondition(patientId, info);
        healthCondition.setProblemId(ProblemType.DIAGNOSTICO);
        if (info.isPresumptive())
            healthCondition.setVerificationStatusId(ConditionVerificationStatus.PRESUMPTIVE);
        updateStatusAndVerification(healthCondition, info);
        LOG.debug(OUTPUT, healthCondition);
        return healthCondition;
    }

    private <T extends HealthConditionBo> HealthCondition updateStatusAndVerification(HealthCondition healthCondition, T newDiagnosis) {
        if (newDiagnosis.isError()) {
            healthCondition.setStatusId(ConditionClinicalStatus.INACTIVE);
            healthCondition.setVerificationStatusId(newDiagnosis.getVerificationId());
            healthCondition.setInactivationDate(dateTimeProvider.nowDate());
        }
        if (newDiagnosis.isDiscarded()) {
            healthCondition.setStatusId(newDiagnosis.getStatusId());
            healthCondition.setVerificationStatusId(newDiagnosis.getVerificationId());
            healthCondition.setInactivationDate(dateTimeProvider.nowDate());
        }
        return healthCondition;
    }

    @Override
    public List<HealthHistoryConditionBo> loadPersonalHistories(Integer patientId, Long documentId, List<HealthHistoryConditionBo> personalHistories) {
        LOG.debug("Input parameters -> patientId {}, documentId {}, personalHistories {}", documentId, patientId, personalHistories);
        personalHistories.forEach(ph -> {
            HealthCondition healthCondition = buildPersonalHistory(patientId, ph);
            healthCondition = save(healthCondition);

            ph.setId(healthCondition.getId());
            ph.setVerificationId(healthCondition.getVerificationStatusId());
            ph.setVerification(getVerification(ph.getVerificationId()));
            ph.setStatusId(healthCondition.getStatusId());
            ph.setStatus(getStatus(ph.getStatusId()));

            documentService.createDocumentHealthCondition(documentId, healthCondition.getId());
        });
        List<HealthHistoryConditionBo> result = personalHistories;
        LOG.debug(OUTPUT, result);
        return result;
    }

    private HealthCondition buildPersonalHistory(Integer patientId, HealthHistoryConditionBo info) {
        LOG.debug(INPUT_PARAMETERS_PATIENT_ID_INFO, patientId, info);
        HealthCondition healthCondition = buildBasicHealthCondition(patientId, info);
        healthCondition.setProblemId(ProblemType.PROBLEMA);
        LocalDate date = info.getDate() == null ? dateTimeProvider.nowDate() : info.getDate();
        healthCondition.setStartDate(date);
        healthCondition.setNoteId(noteService.createNote(info.getNote()));
        LOG.debug(OUTPUT, healthCondition);
        return healthCondition;
    }

    @Override
    public List<HealthHistoryConditionBo> loadFamilyHistories(Integer patientId, Long documentId, List<HealthHistoryConditionBo> familyHistories) {
        LOG.debug("Input parameters -> patientId {}, documentId {}, familyHistories {}", documentId, patientId, familyHistories);
        familyHistories.forEach(ph -> {
            HealthCondition healthCondition = buildFamilyHistory(patientId, ph);
            healthCondition = healthConditionRepository.save(healthCondition);

            ph.setId(healthCondition.getId());
            ph.setVerificationId(healthCondition.getVerificationStatusId());
            ph.setVerification(getVerification(ph.getVerificationId()));
            ph.setStatusId(healthCondition.getStatusId());
            ph.setStatus(getStatus(ph.getStatusId()));

            documentService.createDocumentHealthCondition(documentId, healthCondition.getId());
        });
        List<HealthHistoryConditionBo> result = familyHistories;
        LOG.debug(OUTPUT, result);
        return result;
    }

    private HealthCondition buildFamilyHistory(Integer patientId, HealthHistoryConditionBo info) {
        LOG.debug(INPUT_PARAMETERS_PATIENT_ID_INFO, patientId, info);
        HealthCondition healthCondition = buildBasicHealthCondition(patientId, info);
        healthCondition.setProblemId(ProblemType.ANTECEDENTE);
        LocalDate date = info.getDate() == null ? dateTimeProvider.nowDate() : info.getDate();
        healthCondition.setStartDate(date);
        healthCondition.setNoteId(noteService.createNote(info.getNote()));
        LOG.debug(OUTPUT, healthCondition);
        return healthCondition;
    }

    private HealthCondition buildBasicHealthCondition(Integer patientId, HealthConditionBo info) {
        LOG.debug(INPUT_PARAMETERS_PATIENT_ID_INFO, patientId, info);
        String sctId = snomedService.createSnomedTerm(info.getSnomed());
        HealthCondition healthCondition = new HealthCondition();
        healthCondition.setPatientId(patientId);
        healthCondition.setSctidCode(sctId);
        healthCondition.setStatusId(ConditionClinicalStatus.ACTIVE);
        healthCondition.setVerificationStatusId(info.getVerificationId());
        healthCondition.setStartDate(dateTimeProvider.nowDate());
        LOG.debug(OUTPUT, healthCondition);
        return healthCondition;
    }

    @Override
    public List<Integer> copyDiagnoses(List<Integer> diagnosesId) {
        LOG.debug("Input parameters -> diagnosesId {}", diagnosesId);
        List<HealthCondition> resultQuery = new ArrayList<>();
        if (!diagnosesId.isEmpty())
            resultQuery = healthConditionRepository.findByIds(diagnosesId);

        List<HealthCondition> clonedHc = resultQuery.stream().map(h -> {
                HealthCondition cloned = (HealthCondition) h.clone();
                cloned.setId(null);
                return cloned;
        }).collect(Collectors.toList());
        clonedHc = healthConditionRepository.saveAll(clonedHc);

        List<Integer> result = clonedHc.stream().map(HealthCondition::getId).collect(Collectors.toList());
        LOG.debug(OUTPUT, result);
        return result;
    }

    @Override
    public List<ProblemBo> loadProblems(Integer patientId, Long documentId, List<ProblemBo> problems) {
        LOG.debug("Input parameters -> patientId {}, documentId {}, problems {}", documentId, patientId, problems);
        problems.forEach(ph -> {
            HealthCondition healthCondition = buildProblem(patientId, ph);
            healthCondition = save(healthCondition);

            ph.setId(healthCondition.getId());
            ph.setVerificationId(healthCondition.getVerificationStatusId());
            ph.setVerification(getVerification(ph.getVerificationId()));
            ph.setStatusId(healthCondition.getStatusId());
            ph.setStatus(getStatus(ph.getStatusId()));

            documentService.createDocumentHealthCondition(documentId, healthCondition.getId());
        });
        List<ProblemBo> result = problems;
        LOG.debug(OUTPUT, result);
        return result;
    }

    private HealthCondition buildProblem(Integer patientId, ProblemBo info) {
        LOG.debug("Input parameters -> patientId {}, problem {}", patientId, info);
        HealthCondition healthCondition = buildBasicHealthCondition(patientId, info);
        healthCondition.setProblemId(info.isChronic() ? ProblemType.CHRONIC : ProblemType.PROBLEMA);
        healthCondition.setStartDate(info.getStartDate());
        if (info.getEndDate() != null) {
            healthCondition.setInactivationDate(info.getEndDate());
            healthCondition.setStatusId(ConditionClinicalStatus.SOLVED);
        }
        LOG.debug(OUTPUT, healthCondition);
        return healthCondition;
    }

    private String getVerification(String id) {
        return conditionVerificationStatusRepository.findById(id).map(ConditionVerificationStatus::getDescription).orElse(null);
    }

    private String getStatus(String id) {
        return conditionClinicalStatusRepository.findById(id).map(ConditionClinicalStatus::getDescription).orElse(null);
    }
}
