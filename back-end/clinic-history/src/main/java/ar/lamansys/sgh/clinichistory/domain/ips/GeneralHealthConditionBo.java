package ar.lamansys.sgh.clinichistory.domain.ips;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.hospitalizationState.entity.HealthConditionVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Getter
@Setter
@ToString
public class GeneralHealthConditionBo implements Serializable {

    private static final Logger LOG = LoggerFactory.getLogger(GeneralHealthConditionBo.class);

    public static final String OUTPUT = "Output -> {}";

    private HealthConditionBo mainDiagnosis;

    private List<DiagnosisBo> diagnosis = new ArrayList<>();

    private List<HealthHistoryConditionBo> personalHistories = new ArrayList<>();

    private List<HealthHistoryConditionBo> familyHistories = new ArrayList<>();

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
                this::mapHealthHistoryConditionBo)
        );
        setFamilyHistories(buildGeneralState(
                healthConditionVos,
                HealthConditionVo::isFamilyHistory,
                this::mapHealthHistoryConditionBo));
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
        LOG.debug("Input parameters -> HealthConditionVo {}", healthConditionVo);
        DiagnosisBo result = new DiagnosisBo();
        result.setId(healthConditionVo.getId());
        result.setStatusId(healthConditionVo.getStatusId());
        result.setStatus(healthConditionVo.getStatus());
        result.setVerificationId(healthConditionVo.getVerificationId());
        result.setVerification(healthConditionVo.getVerification());
        result.setSnomed(new SnomedBo(healthConditionVo.getSnomed()));
        result.setPresumptive(healthConditionVo.isPresumptive());
        result.setMain(healthConditionVo.isMain());
        LOG.debug(OUTPUT, result);
        return result;

    }

    private HealthHistoryConditionBo mapHealthHistoryConditionBo(HealthConditionVo healthConditionVo){
        LOG.debug("Input parameters -> HealthConditionVo {}", healthConditionVo);
        HealthHistoryConditionBo result = new HealthHistoryConditionBo();
        result.setId(healthConditionVo.getId());
        result.setStatusId(healthConditionVo.getStatusId());
        result.setStatus(healthConditionVo.getStatus());
        result.setVerificationId(healthConditionVo.getVerificationId());
        result.setVerification(healthConditionVo.getVerification());
        result.setSnomed(new SnomedBo(healthConditionVo.getSnomed()));
        result.setStartDate(healthConditionVo.getStartDate());
        result.setMain(healthConditionVo.isMain());
        LOG.debug(OUTPUT, result);
        return result;

    }

    public HealthConditionBo buildMainDiagnosis(Optional<HealthConditionVo> optionalHealthConditionVo) {
        LOG.debug("Input parameters -> optionalHealthConditionVo {}", optionalHealthConditionVo);
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
        LOG.debug(OUTPUT, result);
        return result.get();
    }

}
