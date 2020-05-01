package net.pladema.internation.service.domain.ips;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import net.pladema.internation.repository.ips.generalstate.HealthConditionVo;
import net.pladema.internation.service.domain.SnomedBo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Getter
@Setter
@ToString
public class GeneralHealthConditionBo implements Serializable {

    private static final Logger LOG = LoggerFactory.getLogger(GeneralHealthConditionBo.class);

    public static final String OUTPUT = "Output -> {}";

    private List<DiagnosisBo> diagnosis = new ArrayList<>();

    private List<HealthHistoryConditionBo> personalHistories = new ArrayList<>();

    private List<HealthHistoryConditionBo> familyHistories = new ArrayList<>();

    public GeneralHealthConditionBo(List<HealthConditionVo> healthConditionVos) {
        setDiagnosis(buildGeneralState(
                healthConditionVos,
                HealthConditionVo::isDiagnosis,
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
        result.setVerificationId(healthConditionVo.getVerificationId());
        result.setSnomed(new SnomedBo(healthConditionVo.getSnomed()));
        result.setPresumptive(healthConditionVo.isPresumptive());
        LOG.debug(OUTPUT, result);
        return result;

    }

    private HealthHistoryConditionBo mapHealthHistoryConditionBo(HealthConditionVo healthConditionVo){
        LOG.debug("Input parameters -> HealthConditionVo {}", healthConditionVo);
        HealthHistoryConditionBo result = new HealthHistoryConditionBo();
        result.setId(healthConditionVo.getId());
        result.setStatusId(healthConditionVo.getStatusId());
        result.setVerificationId(healthConditionVo.getVerificationId());
        result.setSnomed(new SnomedBo(healthConditionVo.getSnomed()));
        result.setDate(healthConditionVo.getStartDate());
        LOG.debug(OUTPUT, result);
        return result;

    }

}
