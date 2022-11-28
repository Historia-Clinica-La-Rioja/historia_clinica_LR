package ar.lamansys.sgh.clinichistory.domain.ips.services;

import ar.lamansys.sgh.clinichistory.application.calculatecie10.CalculateCie10Facade;
import ar.lamansys.sgh.clinichistory.application.calculatecie10.Cie10FacadeRuleFeature;
import ar.lamansys.sgh.clinichistory.application.document.DocumentService;
import ar.lamansys.sgh.clinichistory.application.notes.NoteService;
import ar.lamansys.sgh.clinichistory.domain.ips.*;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.ips.GetLastHealthConditionRepository;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.ips.HealthConditionRepository;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.ips.entity.HealthCondition;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.masterdata.ConditionClinicalStatusRepository;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.masterdata.ConditionVerificationStatusRepository;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.masterdata.entity.ConditionClinicalStatus;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.masterdata.entity.ConditionVerificationStatus;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.masterdata.entity.ProblemType;
import ar.lamansys.sgh.clinichistory.domain.document.PatientInfoBo;
import ar.lamansys.sgx.shared.dates.configuration.DateTimeProvider;
import ar.lamansys.sgx.shared.exceptions.NotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class HealthConditionService {

    public static final String OUTPUT = "Output -> {}";

    private static final Logger LOG = LoggerFactory.getLogger(HealthConditionService.class);
    public static final String INPUT_PARAMETERS_PATIENT_ID_INFO = "Input parameters -> patientId {}, info {}";

    private final HealthConditionRepository healthConditionRepository;

    private final ConditionVerificationStatusRepository conditionVerificationStatusRepository;

    private final ConditionClinicalStatusRepository conditionClinicalStatusRepository;

    private final SnomedService snomedService;

    private final CalculateCie10Facade calculateCie10Facade;

    private final DocumentService documentService;

    private final NoteService noteService;

    private final DateTimeProvider dateTimeProvider;

    private final GetLastHealthConditionRepository getLastHealthConditionRepository;


    public HealthConditionService(HealthConditionRepository healthConditionRepository,
                                  ConditionVerificationStatusRepository conditionVerificationStatusRepository,
                                  ConditionClinicalStatusRepository conditionClinicalStatusRepository,
                                  SnomedService snomedService,
                                  CalculateCie10Facade calculateCie10Facade,
                                  DocumentService documentService,
                                  NoteService noteService, DateTimeProvider dateTimeProvider, GetLastHealthConditionRepository getLastHealthConditionRepository){
        this.healthConditionRepository = healthConditionRepository;
        this.conditionVerificationStatusRepository = conditionVerificationStatusRepository;
        this.conditionClinicalStatusRepository = conditionClinicalStatusRepository;
        this.snomedService = snomedService;
        this.calculateCie10Facade = calculateCie10Facade;
        this.documentService = documentService;
        this.noteService = noteService;
        this.dateTimeProvider = dateTimeProvider;
        this.getLastHealthConditionRepository = getLastHealthConditionRepository;
    }

    private HealthCondition save(HealthCondition healthCondition){
        LOG.debug("Input parameters -> healthCondition {}", healthCondition);
        healthCondition = healthConditionRepository.save(healthCondition);
        LOG.debug(OUTPUT, healthCondition);
        return healthCondition;
    }

    public HealthConditionBo loadMainDiagnosis(PatientInfoBo patientInfo, Long documentId, Optional<HealthConditionBo> mainDiagnosis) {
        LOG.debug("Input parameters -> patientInfo {}, documentId {}, mainDiagnosis {}", patientInfo, documentId, mainDiagnosis);
        mainDiagnosis.ifPresent(md -> {
            HealthCondition healthCondition = buildMainDiagnoses(patientInfo, md);
            if(healthCondition.getId()==null)
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

    private HealthCondition buildMainDiagnoses(PatientInfoBo patientInfo, HealthConditionBo info) {
        LOG.debug("Input parameters -> patientInfo {}, info {}", patientInfo, info);
        HealthCondition healthCondition = buildBasicHealthCondition(patientInfo, info);
        healthCondition.setProblemId(ProblemType.DIAGNOSIS);
        healthCondition.setMain(true);
        updateStatusAndVerification(healthCondition, info);
        LOG.debug(OUTPUT, healthCondition);
        return healthCondition;
    }

    public List<DiagnosisBo> loadDiagnosis(PatientInfoBo patientInfo, Long documentId, List<DiagnosisBo> diagnosis) {
        LOG.debug("Input parameters -> patientInfo {}, documentId {}, diagnosis {}", patientInfo, documentId, diagnosis);
        diagnosis.forEach(d -> {
            HealthCondition healthCondition = buildDiagnoses(patientInfo, d);
			if(healthCondition.getId() == null)
            	healthCondition = save(healthCondition);

            d.setId(healthCondition.getId());
            d.setVerificationId(healthCondition.getVerificationStatusId());
            d.setStatusId(healthCondition.getStatusId());
            d.setVerification(getVerification(d.getVerificationId()));
            d.setStatus(getStatus(d.getStatusId()));

            documentService.createDocumentHealthCondition(documentId, healthCondition.getId());
        });

        LOG.debug(OUTPUT, diagnosis);
        return diagnosis;
    }

    private HealthCondition buildDiagnoses(PatientInfoBo patientInfo, DiagnosisBo info) {
        LOG.debug("Input parameters -> patientInfo {}, info {}", patientInfo, info);
        HealthCondition healthCondition = buildBasicHealthCondition(patientInfo, info);
        healthCondition.setProblemId(ProblemType.DIAGNOSIS);
        if (info.isPresumptive())
            healthCondition.setVerificationStatusId(ConditionVerificationStatus.PRESUMPTIVE);
		else
			healthCondition.setVerificationStatusId(ConditionVerificationStatus.CONFIRMED);
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

    public List<HealthHistoryConditionBo> loadPersonalHistories(PatientInfoBo patientInfo, Long documentId, List<HealthHistoryConditionBo> personalHistories) {
        LOG.debug("Input parameters -> patientInfo {}, documentId {}, personalHistories {}", patientInfo, documentId, personalHistories);
        personalHistories.forEach(ph -> {
            HealthCondition healthCondition = buildPersonalHistory(patientInfo, ph);
			if(ph.getId()==null)
	            healthCondition = save(healthCondition);

            ph.setId(healthCondition.getId());
            ph.setVerificationId(healthCondition.getVerificationStatusId());
            ph.setVerification(getVerification(ph.getVerificationId()));
            ph.setStatusId(healthCondition.getStatusId());
            ph.setStatus(getStatus(ph.getStatusId()));

            documentService.createDocumentHealthCondition(documentId, healthCondition.getId());
        });
        LOG.debug(OUTPUT, personalHistories);
        return personalHistories;
    }

    private HealthCondition buildPersonalHistory(PatientInfoBo patientInfo, HealthHistoryConditionBo info) {
        LOG.debug("Input parameters -> patientInfo {}, info {}", patientInfo, info);
        HealthCondition healthCondition = buildBasicHealthCondition(patientInfo, info);
        healthCondition.setProblemId(ProblemType.PROBLEM);
        LocalDate date = info.getStartDate();
        healthCondition.setStartDate(date);
        healthCondition.setNoteId(noteService.createNote(info.getNote()));
        LOG.debug(OUTPUT, healthCondition);
        return healthCondition;
    }

    public List<HealthHistoryConditionBo> loadFamilyHistories(PatientInfoBo patientInfo, Long documentId, List<HealthHistoryConditionBo> familyHistories) {
        LOG.debug("Input parameters -> patientInfo {}, documentId {}, familyHistories {}", patientInfo, documentId, familyHistories);
        familyHistories.forEach(ph -> {
            HealthCondition healthCondition = buildFamilyHistory(patientInfo, ph);
			if(ph.getId()==null)
	            healthCondition = healthConditionRepository.save(healthCondition);

            ph.setId(healthCondition.getId());
            ph.setVerificationId(healthCondition.getVerificationStatusId());
            ph.setVerification(getVerification(ph.getVerificationId()));
            ph.setStatusId(healthCondition.getStatusId());
            ph.setStatus(getStatus(ph.getStatusId()));

            documentService.createDocumentHealthCondition(documentId, healthCondition.getId());
        });

        LOG.debug(OUTPUT, familyHistories);
        return familyHistories;
    }

    private HealthCondition buildFamilyHistory(PatientInfoBo patientInfo, HealthHistoryConditionBo info) {
        LOG.debug("Input parameters -> patientInfo {}, info {}", patientInfo, info);
        HealthCondition healthCondition = buildBasicHealthCondition(patientInfo, info);
        healthCondition.setProblemId(ProblemType.HISTORY);
        LocalDate date = info.getStartDate();
        healthCondition.setStartDate(date);
        healthCondition.setNoteId(noteService.createNote(info.getNote()));
        LOG.debug(OUTPUT, healthCondition);
        return healthCondition;
    }

    private HealthCondition buildBasicHealthCondition(PatientInfoBo patientInfo, HealthConditionBo info) {
        LOG.debug("Input parameters -> patientInfo {}, info {}", patientInfo, info);
        Integer snomedId = snomedService.getSnomedId(info.getSnomed())
                .orElseGet(() -> snomedService.createSnomedTerm(info.getSnomed()));
        String cie10Codes = calculateCie10Facade.execute(info.getSnomed().getSctid(),
                new Cie10FacadeRuleFeature(patientInfo.getGenderId(), patientInfo.getAge()));
        HealthCondition healthCondition = new HealthCondition();
        healthCondition.setPatientId(patientInfo.getId());
        healthCondition.setSnomedId(snomedId);
        healthCondition.setCie10Codes(cie10Codes);
        healthCondition.setStatusId(ConditionClinicalStatus.ACTIVE);
        healthCondition.setVerificationStatusId(info.getVerificationId());
        healthCondition.setStartDate(dateTimeProvider.nowDate());
		healthCondition.setId(info.getId());
        LOG.debug(OUTPUT, healthCondition);
        return healthCondition;
    }

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

    public List<ProblemBo> loadProblems(PatientInfoBo patientInfo, Long documentId, List<ProblemBo> problems) {
        LOG.debug("Input parameters -> patientInfo {}, documentId {}, problems {}", patientInfo, documentId, problems);
        problems.forEach(ph -> {
            HealthCondition healthCondition = buildProblem(patientInfo, ph);
            healthCondition = save(healthCondition);

            ph.setId(healthCondition.getId());
            ph.setVerificationId(healthCondition.getVerificationStatusId());
            ph.setVerification(getVerification(ph.getVerificationId()));
            ph.setStatusId(healthCondition.getStatusId());
            ph.setStatus(getStatus(ph.getStatusId()));

            documentService.createDocumentHealthCondition(documentId, healthCondition.getId());
        });

        LOG.debug(OUTPUT, problems);
        return problems;
    }

    private HealthCondition buildProblem(PatientInfoBo patientInfo, ProblemBo info) {
        LOG.debug("Input parameters -> patientInfo {}, info {}", patientInfo, info);
        HealthCondition healthCondition = buildBasicHealthCondition(patientInfo, info);
        healthCondition.setProblemId(info.isChronic() ? ProblemType.CHRONIC : ProblemType.PROBLEM);
        healthCondition.setStartDate(info.getStartDate());
        if (info.getEndDate() != null) {
            healthCondition.setInactivationDate(info.getEndDate());
            healthCondition.setStatusId(ConditionClinicalStatus.SOLVED);
        }
        healthCondition.setSeverity(info.getSeverity());
        LOG.debug(OUTPUT, healthCondition);
        return healthCondition;
    }

    private String getVerification(String id) {
        return conditionVerificationStatusRepository.findById(id).map(ConditionVerificationStatus::getDescription).orElse(null);
    }

    private String getStatus(String id) {
        return conditionClinicalStatusRepository.findById(id).map(ConditionClinicalStatus::getDescription).orElse(null);
    }

    public HealthConditionNewConsultationBo getHealthCondition(Integer healthConditionId){
        HealthCondition hc = this.healthConditionRepository.findById(healthConditionId)
                .orElseThrow(()->new NotFoundException("healthcondition-not-found", "Healthcondition not found"));

        HealthConditionNewConsultationBo newConsultationBo = new HealthConditionNewConsultationBo(hc);
        newConsultationBo.setSnomed(snomedService.getSnomed(hc.getSnomedId()));
        return newConsultationBo;
    }

    public Map<Integer, HealthConditionBo> getLastHealthCondition(Integer patientId, List<Integer> hcIds) {
        LOG.debug("Input -> patientId {} hcIds {}", patientId, hcIds);

        Map<Integer, HealthConditionBo> result = new HashMap<>();

        var repoResult = getLastHealthConditionRepository.run(patientId, hcIds);

        repoResult.forEach(row -> {
            Integer originalHcId = (Integer) row[0];
            HealthConditionBo updatedHc =  buildHealthConditionBo(row);
            result.put(originalHcId, updatedHc);
        });

        LOG.trace(OUTPUT, result);
        return result;
    }

    public HealthConditionBo buildHealthConditionBo(Object[] row){
        LOG.debug("Input parameters -> row {}", row);

        HealthConditionBo result = new HealthConditionBo();
        result.setId((Integer) row[1]);
        result.setStatusId((String) row[2]);

        LOG.trace(OUTPUT, result);
        return result;
    }

}
