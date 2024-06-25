package ar.lamansys.sgh.clinichistory.domain.ips.services;

import ar.lamansys.sgh.clinichistory.application.calculatecie10.CalculateCie10Facade;
import ar.lamansys.sgh.clinichistory.application.calculatecie10.Cie10FacadeRuleFeature;
import ar.lamansys.sgh.clinichistory.application.document.DocumentService;
import ar.lamansys.sgh.clinichistory.application.notes.NoteService;
import ar.lamansys.sgh.clinichistory.domain.ReferableItemBo;
import ar.lamansys.sgh.clinichistory.domain.ips.DiagnosisBo;
import ar.lamansys.sgh.clinichistory.domain.ips.FamilyHistoryBo;
import ar.lamansys.sgh.clinichistory.domain.ips.HealthConditionBo;
import ar.lamansys.sgh.clinichistory.domain.ips.HealthConditionNewConsultationBo;
import ar.lamansys.sgh.clinichistory.domain.ips.PersonalHistoryBo;
import ar.lamansys.sgh.clinichistory.domain.ips.ProblemBo;
import ar.lamansys.sgh.clinichistory.domain.ips.enums.EPersonalHistoryType;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.ips.GetLastHealthConditionRepository;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.ips.HealthConditionRepository;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.ips.PersonalHistoryRepository;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.ips.entity.HealthCondition;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.ips.entity.PersonalHistory;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.masterdata.ConditionClinicalStatusRepository;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.masterdata.ConditionVerificationStatusRepository;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.masterdata.entity.ConditionClinicalStatus;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.masterdata.entity.ConditionVerificationStatus;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.masterdata.entity.ProblemType;
import ar.lamansys.sgh.clinichistory.domain.document.PatientInfoBo;
import ar.lamansys.sgx.shared.dates.configuration.DateTimeProvider;
import ar.lamansys.sgx.shared.exceptions.NotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.validation.Valid;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import static java.util.Objects.nonNull;

@Slf4j
@RequiredArgsConstructor
@Service
public class HealthConditionService {

    public static final String OUTPUT = "Output -> {}";

    private final HealthConditionRepository healthConditionRepository;

    private final ConditionVerificationStatusRepository conditionVerificationStatusRepository;

    private final ConditionClinicalStatusRepository conditionClinicalStatusRepository;

    private final SnomedService snomedService;

    private final CalculateCie10Facade calculateCie10Facade;

    private final DocumentService documentService;

    private final NoteService noteService;

    private final DateTimeProvider dateTimeProvider;

    private final GetLastHealthConditionRepository getLastHealthConditionRepository;

    private final PersonalHistoryRepository personalHistoryRepository;



    private HealthCondition save(HealthCondition healthCondition){
        log.debug("Input parameters -> healthCondition {}", healthCondition);
        healthCondition = healthConditionRepository.save(healthCondition);
        log.debug(OUTPUT, healthCondition);
        return healthCondition;
    }

    public HealthConditionBo loadMainDiagnosis(PatientInfoBo patientInfo, Long documentId, Optional<HealthConditionBo> mainDiagnosis) {
        log.debug("Input parameters -> patientInfo {}, documentId {}, mainDiagnosis {}", patientInfo, documentId, mainDiagnosis);
        mainDiagnosis.ifPresent(md -> {
            HealthCondition healthCondition = buildMainDiagnoses(patientInfo, md);
			healthCondition.setId(null);
			healthCondition = save(healthCondition);
            md.setId(healthCondition.getId());
            md.setVerificationId(healthCondition.getVerificationStatusId());
            md.setStatusId(healthCondition.getStatusId());
            md.setVerification(getVerification(md.getVerificationId()));
            md.setStatus(getStatus(md.getStatusId()));
			healthConditionRepository.setMain(healthCondition.getId(), true);
            documentService.createDocumentHealthCondition(documentId, healthCondition.getId());
        });
        HealthConditionBo result = mainDiagnosis.orElse(null);
        log.debug(OUTPUT, result);
        return result;
    }

    private HealthCondition buildMainDiagnoses(PatientInfoBo patientInfo, HealthConditionBo info) {
        log.debug("Input parameters -> patientInfo {}, info {}", patientInfo, info);
        HealthCondition healthCondition = buildBasicHealthCondition(patientInfo, info);
        healthCondition.setProblemId(ProblemType.DIAGNOSIS);
        healthCondition.setMain(true);
        updateStatusAndVerification(healthCondition, info);
        log.debug(OUTPUT, healthCondition);
        return healthCondition;
    }

    public List<DiagnosisBo> loadDiagnosis(PatientInfoBo patientInfo, Long documentId, List<DiagnosisBo> diagnosis) {
        log.debug("Input parameters -> patientInfo {}, documentId {}, diagnosis {}", patientInfo, documentId, diagnosis);
        diagnosis.forEach(d -> {
            HealthCondition healthCondition = buildDiagnoses(patientInfo, d);
			healthCondition.setId(null);
			healthCondition = save(healthCondition);
			d.setId(healthCondition.getId());
			d.setVerificationId(healthCondition.getVerificationStatusId());
			d.setStatusId(healthCondition.getStatusId());
			d.setVerification(getVerification(d.getVerificationId()));
			d.setStatus(getStatus(d.getStatusId()));
			healthConditionRepository.setMain(healthCondition.getId(), false);
            documentService.createDocumentHealthCondition(documentId, healthCondition.getId());
        });

        log.debug(OUTPUT, diagnosis);
        return diagnosis;
    }

    private HealthCondition buildDiagnoses(PatientInfoBo patientInfo, DiagnosisBo info) {
        log.debug("Input parameters -> patientInfo {}, info {}", patientInfo, info);
        HealthCondition healthCondition = buildBasicHealthCondition(patientInfo, info);
        healthCondition.setProblemId(info.getType().getId());
        if (info.isPresumptive())
            healthCondition.setVerificationStatusId(ConditionVerificationStatus.PRESUMPTIVE);
		else
			healthCondition.setVerificationStatusId(ConditionVerificationStatus.CONFIRMED);
        updateStatusAndVerification(healthCondition, info);
        log.debug(OUTPUT, healthCondition);
        return healthCondition;
    }

    private <T extends HealthConditionBo> void updateStatusAndVerification(HealthCondition healthCondition, T newHealthCondition) {
        if (newHealthCondition.isError()) {
            healthCondition.setStatusId(ConditionClinicalStatus.INACTIVE);
            healthCondition.setVerificationStatusId(newHealthCondition.getVerificationId());
            healthCondition.setInactivationDate(dateTimeProvider.nowDate());
        }
        if (newHealthCondition.isDiscarded()) {
            healthCondition.setStatusId(newHealthCondition.getStatusId());
            healthCondition.setVerificationStatusId(newHealthCondition.getVerificationId());
            healthCondition.setInactivationDate(dateTimeProvider.nowDate());
        }
    }

    public List<PersonalHistoryBo> loadPersonalHistories(PatientInfoBo patientInfo, Long documentId, ReferableItemBo<PersonalHistoryBo> personalHistories) {
        log.debug("Input parameters -> patientInfo {}, documentId {}, personalHistories {}", patientInfo, documentId, personalHistories);
		if (personalHistories != null)
			return getPersonalHistory(patientInfo, documentId, personalHistories);
		return new ArrayList<>();
    }

	private List<@Valid PersonalHistoryBo> getPersonalHistory(PatientInfoBo patientInfo, Long documentId, ReferableItemBo<PersonalHistoryBo> personalHistories) {
		documentService.createDocumentRefersPersonalHistory(documentId, personalHistories.getIsReferred());
		if (personalHistories.getContent() != null)
			personalHistories.getContent().forEach(ph -> processPersonalHistory(patientInfo, documentId, ph));
		log.debug(OUTPUT, personalHistories);
		return personalHistories.getContent();
	}

	private void processPersonalHistory(PatientInfoBo patientInfo, Long documentId, PersonalHistoryBo ph) {
		HealthCondition healthCondition = buildPersonalHistory(patientInfo, ph);
		if (ph.getId() == null)
			healthCondition = getHealthCondition(ph, healthCondition);

		ph.setId(healthCondition.getId());
		ph.setVerificationId(healthCondition.getVerificationStatusId());
		ph.setVerification(getVerification(ph.getVerificationId()));
		ph.setStatusId(healthCondition.getStatusId());
		ph.setStatus(getStatus(ph.getStatusId()));
		ph.setType(nonNull(ph.getTypeId()) ? EPersonalHistoryType.map(ph.getTypeId()).getDescription() : null);

		documentService.createDocumentHealthCondition(documentId, healthCondition.getId());
	}

	private HealthCondition getHealthCondition(PersonalHistoryBo ph, HealthCondition healthCondition) {
		healthCondition = save(healthCondition);
		if (nonNull(ph.getTypeId()))
			personalHistoryRepository.save(new PersonalHistory(healthCondition.getId(), ph.getTypeId()));
		return healthCondition;
	}

	private HealthCondition buildPersonalHistory(PatientInfoBo patientInfo, PersonalHistoryBo info) {
        log.debug("Input parameters -> patientInfo {}, info {}", patientInfo, info);
        HealthCondition healthCondition = buildBasicHealthCondition(patientInfo, info);
        healthCondition.setProblemId(ProblemType.PERSONAL_HISTORY);
        healthCondition.setStartDate(info.getStartDate());
        healthCondition.setInactivationDate(info.getInactivationDate());
        healthCondition.setNoteId(noteService.createNote(info.getNote()));
        log.debug(OUTPUT, healthCondition);
        return healthCondition;
    }

    public List<FamilyHistoryBo> loadFamilyHistories(PatientInfoBo patientInfo, Long documentId, ReferableItemBo<FamilyHistoryBo> familyHistories) {
        log.debug("Input parameters -> patientInfo {}, documentId {}, familyHistories {}", patientInfo, documentId, familyHistories);
		if (familyHistories != null)
			return getFamilyHistory(patientInfo, documentId, familyHistories);
		return new ArrayList<>();
    }

	private List<@Valid FamilyHistoryBo> getFamilyHistory(PatientInfoBo patientInfo, Long documentId, ReferableItemBo<FamilyHistoryBo> familyHistories) {
		documentService.createDocumentRefersFamilyHistory(documentId, familyHistories.getIsReferred());
		if (familyHistories.getContent() != null)
			familyHistories.getContent().forEach(ph -> processFamilyHistory(patientInfo, documentId, ph));
		log.debug(OUTPUT, familyHistories);
		return familyHistories.getContent();
	}

	private void processFamilyHistory(PatientInfoBo patientInfo, Long documentId, FamilyHistoryBo ph) {
		HealthCondition healthCondition = buildFamilyHistory(patientInfo, ph);
		if(ph.getId()==null)
			healthCondition = healthConditionRepository.save(healthCondition);

		ph.setId(healthCondition.getId());
		ph.setVerificationId(healthCondition.getVerificationStatusId());
		ph.setVerification(getVerification(ph.getVerificationId()));
		ph.setStatusId(healthCondition.getStatusId());
		ph.setStatus(getStatus(ph.getStatusId()));

		documentService.createDocumentHealthCondition(documentId, healthCondition.getId());
	}

	private HealthCondition buildFamilyHistory(PatientInfoBo patientInfo, FamilyHistoryBo info) {
        log.debug("Input parameters -> patientInfo {}, info {}", patientInfo, info);
        HealthCondition healthCondition = buildBasicHealthCondition(patientInfo, info);
        healthCondition.setProblemId(ProblemType.FAMILY_HISTORY);
        LocalDate date = info.getStartDate();
        healthCondition.setStartDate(date);
        healthCondition.setNoteId(noteService.createNote(info.getNote()));
        log.debug(OUTPUT, healthCondition);
        return healthCondition;
    }

	public List<HealthConditionBo> loadOtherProblems(PatientInfoBo patientInfo, Long documentId, List<HealthConditionBo> otherProblems){
		log.debug("Input parameters -> patientInfo {}, documentId {}, otherProblems {}", patientInfo, documentId, otherProblems);
		otherProblems.forEach(op -> {
			HealthCondition healthCondition = buildOtherProblem(patientInfo, op);
			if (op.getId() == null)
				healthCondition = healthConditionRepository.save(healthCondition);

			op.setId(healthCondition.getId());
			op.setVerificationId(healthCondition.getVerificationStatusId());
			op.setVerification(getVerification(op.getVerificationId()));
			op.setStatusId(healthCondition.getStatusId());
			op.setStatus(getStatus(op.getStatusId()));

			documentService.createDocumentHealthCondition(documentId, healthCondition.getId());
		});

		log.debug(OUTPUT, otherProblems);
		return otherProblems;
	}

	private HealthCondition buildOtherProblem (PatientInfoBo patientInfo, HealthConditionBo info){
		log.debug("Input parameters -> patientInfo {}, info {}", patientInfo, info);
		HealthCondition healthCondition = this.buildBasicHealthCondition(patientInfo, info);
		healthCondition.setProblemId(ProblemType.OTHER);
        this.updateStatusAndVerification(healthCondition, info);
		log.debug(OUTPUT, healthCondition);
		return healthCondition;
	}

    public List<HealthConditionBo> loadOtherHistories(PatientInfoBo patientInfo, Long documentId, List<HealthConditionBo> otherHistories){
        log.debug("Input parameters -> patientInfo {}, documentId {}, otherHistories {}", patientInfo, documentId, otherHistories);
        otherHistories.forEach(op -> {
            HealthCondition healthCondition = buildOtherHistory(patientInfo, op);
            if (op.getId() == null)
                healthCondition = healthConditionRepository.save(healthCondition);

            op.setId(healthCondition.getId());
            op.setVerificationId(healthCondition.getVerificationStatusId());
            op.setVerification(getVerification(op.getVerificationId()));
            op.setStatusId(healthCondition.getStatusId());
            op.setStatus(getStatus(op.getStatusId()));

            documentService.createDocumentHealthCondition(documentId, healthCondition.getId());
        });

        log.debug(OUTPUT, otherHistories);
        return otherHistories;
    }

    private HealthCondition buildOtherHistory(PatientInfoBo patientInfo, HealthConditionBo info){
        log.debug("Input parameters -> patientInfo {}, info {}", patientInfo, info);
        HealthCondition healthCondition = buildBasicHealthCondition(patientInfo, info);
        healthCondition.setProblemId(ProblemType.OTHER_HISTORY);
        log.debug(OUTPUT, healthCondition);
        return healthCondition;
    }

    private HealthCondition buildBasicHealthCondition(PatientInfoBo patientInfo, HealthConditionBo info) {
        log.debug("Input parameters -> patientInfo {}, info {}", patientInfo, info);
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
        log.debug(OUTPUT, healthCondition);
        return healthCondition;
    }

    public List<Integer> copyDiagnoses(List<Integer> diagnosesId) {
        log.debug("Input parameters -> diagnosesId {}", diagnosesId);
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
        log.debug(OUTPUT, result);
        return result;
    }

    public List<ProblemBo> loadProblems(PatientInfoBo patientInfo, Long documentId, List<ProblemBo> problems) {
        log.debug("Input parameters -> patientInfo {}, documentId {}, problems {}", patientInfo, documentId, problems);
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

        log.debug(OUTPUT, problems);
        return problems;
    }

    private HealthCondition buildProblem(PatientInfoBo patientInfo, ProblemBo info) {
        log.debug("Input parameters -> patientInfo {}, info {}", patientInfo, info);
        HealthCondition healthCondition = buildBasicHealthCondition(patientInfo, info);
        healthCondition.setProblemId(info.isChronic() ? ProblemType.CHRONIC : ProblemType.PROBLEM);
        healthCondition.setStartDate(info.getStartDate());
        if (info.getEndDate() != null) {
            healthCondition.setInactivationDate(info.getEndDate());
            healthCondition.setStatusId(ConditionClinicalStatus.SOLVED);
        }
        healthCondition.setSeverity(info.getSeverity());
        log.debug(OUTPUT, healthCondition);
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
        log.debug("Input -> patientId {} hcIds {}", patientId, hcIds);

        Map<Integer, HealthConditionBo> result = new HashMap<>();

        var repoResult = getLastHealthConditionRepository.run(patientId, hcIds);

        repoResult.forEach(row -> {
            Integer originalHcId = (Integer) row[0];
            HealthConditionBo updatedHc =  buildHealthConditionBo(row);
            result.put(originalHcId, updatedHc);
        });

        log.trace(OUTPUT, result);
        return result;
    }

    public HealthConditionBo buildHealthConditionBo(Object[] row){
        log.debug("Input parameters -> row {}", row);

        HealthConditionBo result = new HealthConditionBo();
        result.setId((Integer) row[1]);
        result.setStatusId((String) row[2]);

        log.trace(OUTPUT, result);
        return result;
    }

	private HealthCondition getById(Integer healthConditionId){
		return this.healthConditionRepository.findById(healthConditionId)
				.orElseThrow(()->new NotFoundException("healthcondition-not-found", "Healthcondition not found"));
	}


}
