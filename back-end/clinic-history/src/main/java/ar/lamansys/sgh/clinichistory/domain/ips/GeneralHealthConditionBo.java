package ar.lamansys.sgh.clinichistory.domain.ips;

import ar.lamansys.sgh.clinichistory.domain.ips.enums.EPersonalHistoryType;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.hospitalizationState.entity.HealthConditionVo;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.masterdata.entity.EProblemErrorReason;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import ar.lamansys.sgh.shared.infrastructure.input.service.ProblemTypeEnum;

import static java.util.Objects.nonNull;

@Getter
@Setter
@Slf4j
@ToString
public class GeneralHealthConditionBo implements Serializable {

    public static final String OUTPUT = "Output -> {}";

    private HealthConditionBo mainDiagnosis;

    private List<DiagnosisBo> diagnosis = new ArrayList<>();

    private List<PersonalHistoryBo> personalHistories = new ArrayList<>();

    private List<FamilyHistoryBo> familyHistories = new ArrayList<>();

	private List<ProblemBo> problems = new ArrayList<>();

	private List<HealthConditionBo> otherProblems = new ArrayList<>();

	private List<DiagnosisBo> preoperativeDiagnosis = new ArrayList<>();

	private List<DiagnosisBo> postoperativeDiagnosis = new ArrayList<>();

	public GeneralHealthConditionBo(List<HealthConditionVo> healthConditionVos) {
        setMainDiagnosis(buildMainDiagnosis(healthConditionVos.stream().filter(HealthConditionVo::isMain).findAny()));
        setDiagnosis(buildGeneralState(
                healthConditionVos,
                HealthConditionVo::isSecondaryDiagnosis,
                this::mapDiagnosis)
        );
        setPersonalHistories(buildGeneralState(
                healthConditionVos,
                HealthConditionVo::isPersonalHistory,
                this::mapPersonalHistoryBo)
        );
        setFamilyHistories(buildGeneralState(
                healthConditionVos,
                HealthConditionVo::isFamilyHistory,
                this::mapFamilyHistoryBo));
		setProblems(buildGeneralState(
				healthConditionVos,
				HealthConditionVo::isProblem,
				this::buildProblem));
		setOtherProblems(buildGeneralState(
				healthConditionVos,
				healthConditionVo -> healthConditionVo.isOfType(ProblemTypeEnum.OTHER),
				this::mapToHealthConditionBo
		));
		setPreoperativeDiagnosis(buildGeneralState(
				healthConditionVos,
				healthConditionVo -> healthConditionVo.isOfType(ProblemTypeEnum.PREOPERATIVE_DIAGNOSIS),
				this::mapDiagnosis
		));
		setPostoperativeDiagnosis(buildGeneralState(
				healthConditionVos,
				healthConditionVo -> healthConditionVo.isOfType(ProblemTypeEnum.POSTOPERATIVE_DIAGNOSIS),
				this::mapDiagnosis
		));
    }
	private <T extends HealthConditionBo> List<T> buildGeneralState(List<HealthConditionVo> data,
                                                                    Predicate<? super HealthConditionVo> filterFunction,
                                                                    Function<? super HealthConditionVo, ? extends T> mapFunction){
        return data.stream()
                .filter(filterFunction)
                .map(mapFunction)
                .collect(Collectors.toList());

    }

    private DiagnosisBo mapDiagnosis(HealthConditionVo healthConditionVo){
        log.debug("Input parameters -> HealthConditionVo {}", healthConditionVo);
        DiagnosisBo result = new DiagnosisBo();
        result.setId(healthConditionVo.getId());
        result.setStatusId(healthConditionVo.getStatusId());
        result.setStatus(healthConditionVo.getStatus());
        result.setVerificationId(healthConditionVo.getVerificationId());
        result.setVerification(healthConditionVo.getVerification());
        result.setSnomed(new SnomedBo(healthConditionVo.getSnomed()));
        result.setPresumptive(healthConditionVo.isPresumptive());
        result.setMain(healthConditionVo.isMain());
		result.setType(ProblemTypeEnum.map(healthConditionVo.getProblemId()));
        log.debug(OUTPUT, result);
        return result;

    }

    private PersonalHistoryBo mapPersonalHistoryBo(HealthConditionVo healthConditionVo){
        log.debug("Input parameters -> HealthConditionVo {}", healthConditionVo);
        PersonalHistoryBo result = new PersonalHistoryBo();
        result.setId(healthConditionVo.getId());
        result.setStatusId(healthConditionVo.getStatusId());
        result.setStatus(healthConditionVo.getStatus());
        result.setVerificationId(healthConditionVo.getVerificationId());
        result.setVerification(healthConditionVo.getVerification());
        result.setSnomed(new SnomedBo(healthConditionVo.getSnomed()));
        result.setStartDate(healthConditionVo.getStartDate());
        result.setInactivationDate(healthConditionVo.getEndDate());
        result.setNote(healthConditionVo.getNote());
        result.setMain(healthConditionVo.isMain());
        result.setType(nonNull(healthConditionVo.getSpecificType()) ? EPersonalHistoryType.map(healthConditionVo.getSpecificType()).getDescription() : null);
        log.debug(OUTPUT, result);
        return result;

    }

    private FamilyHistoryBo mapFamilyHistoryBo(HealthConditionVo healthConditionVo){
        log.debug("Input parameters -> HealthConditionVo {}", healthConditionVo);
        FamilyHistoryBo result = new FamilyHistoryBo();
        result.setId(healthConditionVo.getId());
        result.setStatusId(healthConditionVo.getStatusId());
        result.setStatus(healthConditionVo.getStatus());
        result.setVerificationId(healthConditionVo.getVerificationId());
        result.setVerification(healthConditionVo.getVerification());
        result.setSnomed(new SnomedBo(healthConditionVo.getSnomed()));
        result.setStartDate(healthConditionVo.getStartDate());
        result.setMain(healthConditionVo.isMain());
        log.debug(OUTPUT, result);
        return result;

    }

    public HealthConditionBo buildMainDiagnosis(Optional<HealthConditionVo> optionalHealthConditionVo) {
        log.debug("Input parameters -> optionalHealthConditionVo {}", optionalHealthConditionVo);
        AtomicReference<HealthConditionBo> result = new AtomicReference<>(null);
        optionalHealthConditionVo.ifPresent(healthConditionVo -> {
            result.set(new HealthConditionBo());
            result.get().setId(healthConditionVo.getId());
            result.get().setStatusId(healthConditionVo.getStatusId());
            result.get().setStatus(healthConditionVo.getStatus());
            result.get().setVerificationId(healthConditionVo.getVerificationId());
            result.get().setVerification(healthConditionVo.getVerification());
            result.get().setSnomed(new SnomedBo(healthConditionVo.getSnomed()));
            result.get().setMain(healthConditionVo.isMain());
        });
        log.debug(OUTPUT, result);
        return result.get();
    }

	public ProblemBo buildProblem(HealthConditionVo healthConditionVo) {
		log.debug("Input parameters -> healthConditionVo {}", healthConditionVo);
		ProblemBo result = new ProblemBo();
		result.setId(healthConditionVo.getId());
		result.setStatusId(healthConditionVo.getStatusId());
		result.setStatus(healthConditionVo.getStatus());
		result.setVerificationId(healthConditionVo.getVerificationId());
		result.setVerification(healthConditionVo.getVerification());
		result.setSnomed(new SnomedBo(healthConditionVo.getSnomed()));
		result.setMain(healthConditionVo.isMain());
		result.setStartDate(healthConditionVo.getStartDate());
		result.setChronic(healthConditionVo.isOfType(ProblemTypeEnum.CHRONIC));
        result.setEndDate(healthConditionVo.getEndDate());
        result.setErrorReason(nonNull(healthConditionVo.getErrorReasonId()) ? EProblemErrorReason.map(healthConditionVo.getErrorReasonId()).getDescription() : null);
        result.setErrorObservations(healthConditionVo.getNote());
		log.debug(OUTPUT, result);
		return result;
	}

	private HealthConditionBo mapToHealthConditionBo(HealthConditionVo healthConditionVo){
		log.debug("Input parameters -> HealthConditionVo {}", healthConditionVo);
		HealthHistoryConditionBo result = new HealthHistoryConditionBo();
		result.setId(healthConditionVo.getId());
		result.setStatusId(healthConditionVo.getStatusId());
		result.setStatus(healthConditionVo.getStatus());
		result.setVerificationId(healthConditionVo.getVerificationId());
		result.setVerification(healthConditionVo.getVerification());
		result.setSnomed(new SnomedBo(healthConditionVo.getSnomed()));
		result.setMain(healthConditionVo.isMain());
		log.debug(OUTPUT, result);
		return result;
	}

}
