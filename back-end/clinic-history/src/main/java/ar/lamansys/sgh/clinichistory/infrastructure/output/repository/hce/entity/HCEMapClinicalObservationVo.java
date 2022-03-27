package ar.lamansys.sgh.clinichistory.infrastructure.output.repository.hce.entity;

import ar.lamansys.sgh.clinichistory.domain.hce.HCEAnthropometricDataBo;
import ar.lamansys.sgh.clinichistory.domain.hce.HCEClinicalObservationBo;
import ar.lamansys.sgh.clinichistory.domain.hce.HCERiskFactorBo;
import ar.lamansys.sgh.clinichistory.domain.ips.EObservationLab;
import ar.lamansys.sgh.clinichistory.domain.ips.ERiskFactor;
import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.stream.Collectors;

@Getter
public class HCEMapClinicalObservationVo {

    private static final Logger LOG = LoggerFactory.getLogger(HCEMapClinicalObservationVo.class);

    public static final String OUTPUT = "Output -> {}";

    private Map<String, List<HCEClinicalObservationVo>> clinicalObservationByCode;

    public HCEMapClinicalObservationVo(List<HCEClinicalObservationVo> RiskFactorVos){
        super();
        clinicalObservationByCode = RiskFactorVos.stream().collect(Collectors.groupingBy(HCEClinicalObservationVo::getSctidCode));
        clinicalObservationByCode.forEach(this::processData);
    }

    private void processData(String key, List<HCEClinicalObservationVo> inputValues) {
        LOG.debug("Input parameters -> key {}, RiskFactors {}", key, inputValues);
        inputValues.sort(Comparator.comparing(HCEClinicalObservationVo::getEffectiveTime).reversed());
        List<HCEClinicalObservationVo> result = new ArrayList<>();
        int historyLength = 2;
        int index = 0;
        int deleteQuantity = 0;
        while (historyLength > 0 && index < inputValues.size()) {
            while (index < inputValues.size()) {
                if (!inputValues.get(index).hasError() && deleteQuantity == 0) {
                    result.add(inputValues.get(index));
                    historyLength--;
                }
                if (!inputValues.get(index).hasError() && deleteQuantity > 0) {
                    deleteQuantity--;
                }
                if (inputValues.get(index).hasError())
                    deleteQuantity++;
                index++;
            }
        }
        inputValues.clear();
        inputValues.addAll(result);
        LOG.debug(OUTPUT, result);
    }

    private Optional<HCEClinicalObservationVo> getLastNClinicalObservationByCode(String sctidCode, Integer pos){
        List<HCEClinicalObservationVo> sortedRiskFactor = clinicalObservationByCode.get(sctidCode);
        if (sortedRiskFactor == null || sortedRiskFactor.isEmpty() || sortedRiskFactor.size() <= pos)
            return Optional.empty();
        return Optional.of(sortedRiskFactor.get(pos));
    }

    public List<HCEClinicalObservationVo> getClinicalObservationByCode(String key){
        return clinicalObservationByCode.getOrDefault(key, new ArrayList<>());
    }

    public Optional<HCERiskFactorBo> getLastRiskFactors() {
        return getLastNRiskFactors(0);
    }

    public Optional<HCERiskFactorBo> getLastNRiskFactors(int i) {
        LOG.debug("Input parameters -> pos {}", i);
        HCERiskFactorBo RiskFactorBo = new HCERiskFactorBo();
        getLastNClinicalObservationByCode(ERiskFactor.BLOOD_OXYGEN_SATURATION.getSctidCode(),i).ifPresent(v ->
            RiskFactorBo.setBloodOxygenSaturation(new HCEClinicalObservationBo(v))
        );
        getLastNClinicalObservationByCode(ERiskFactor.DIASTOLIC_BLOOD_PRESSURE.getSctidCode(),i).ifPresent(v ->
            RiskFactorBo.setDiastolicBloodPressure(new HCEClinicalObservationBo(v))
        );
        getLastNClinicalObservationByCode(ERiskFactor.SYSTOLIC_BLOOD_PRESSURE.getSctidCode(),i).ifPresent(v ->
            RiskFactorBo.setSystolicBloodPressure(new HCEClinicalObservationBo(v))
        );
        getLastNClinicalObservationByCode(ERiskFactor.BLOOD_GLUCOSE.getSctidCode(),i).ifPresent(v ->
                RiskFactorBo.setBloodGlucose(new HCEClinicalObservationBo(v))
        );
        getLastNClinicalObservationByCode(ERiskFactor.GLYCOSYLATED_HEMOGLOBIN.getSctidCode(),i).ifPresent(v ->
                RiskFactorBo.setGlycosylatedHemoglobin(new HCEClinicalObservationBo(v))
        );
        getLastNClinicalObservationByCode(ERiskFactor.CARDIOVASCULAR_RISK.getSctidCode(),i).ifPresent(v ->
                RiskFactorBo.setCardiovascularRisk(new HCEClinicalObservationBo(v))
        );
        getLastNClinicalObservationByCode(ERiskFactor.HEART_RATE.getSctidCode(),i).ifPresent(v ->
            RiskFactorBo.setHeartRate(new HCEClinicalObservationBo(v))
        );
        getLastNClinicalObservationByCode(ERiskFactor.TEMPERATURE.getSctidCode(),i).ifPresent(v ->
            RiskFactorBo.setTemperature(new HCEClinicalObservationBo(v))
        );
        getLastNClinicalObservationByCode(ERiskFactor.RESPIRATORY_RATE.getSctidCode(),i).ifPresent(v ->
            RiskFactorBo.setRespiratoryRate(new HCEClinicalObservationBo(v))
        );
        getLastNClinicalObservationByCode(ERiskFactor.MEAN_PRESSURE.getSctidCode(),i).ifPresent(v ->
            RiskFactorBo.setMeanPressure(new HCEClinicalObservationBo(v))
        );
        LOG.debug(OUTPUT, RiskFactorBo);
        if (RiskFactorBo.hasValues())
            return Optional.of(RiskFactorBo);
        return Optional.empty();
    }

    public Optional<HCEAnthropometricDataBo> getLastAnthropometricData() {
        return getLastNAnthropometricData(0);
    }

    public Optional<HCEAnthropometricDataBo> getLastNAnthropometricData(int i) {
        LOG.debug("Input parameters -> pos {}", i);
        HCEAnthropometricDataBo result = new HCEAnthropometricDataBo();
        getLastNClinicalObservationByCode(ERiskFactor.HEIGHT.getSctidCode(),i).ifPresent(v ->
            result.setHeight(new HCEClinicalObservationBo(v))
        );
        getLastNClinicalObservationByCode(ERiskFactor.WEIGHT.getSctidCode(),i).ifPresent(v ->
            result.setWeight(new HCEClinicalObservationBo(v))
        );
        getLastNClinicalObservationByCode(ERiskFactor.HEAD_CIRCUMFERENCE.getSctidCode(),i).ifPresent(v ->
                result.setHeadCircumference(new HCEClinicalObservationBo(v))
        );
        getLastNClinicalObservationByCode(EObservationLab.BLOOD_TYPE.getSctidCode(),i).ifPresent(v ->
            result.setBloodType(new HCEClinicalObservationBo(v))
        );
        LOG.debug(OUTPUT, result);
        if (result.hasValues())
            return Optional.of(result);
        return Optional.empty();
    }

}
