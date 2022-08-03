package ar.lamansys.sgh.clinichistory.domain.ips;

import lombok.Getter;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.hospitalizationState.entity.ClinicalObservationVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.stream.Collectors;

@Getter
public class MapClinicalObservationVo {

    private static final Logger LOG = LoggerFactory.getLogger(MapClinicalObservationVo.class);

    public static final String OUTPUT = "Output -> {}";

    private Map<String, List<ClinicalObservationVo>> clinicalObservationByCode;

    public MapClinicalObservationVo(List<ClinicalObservationVo> RiskFactorVos){
        super();
        clinicalObservationByCode = RiskFactorVos.stream().collect(Collectors.groupingBy(ClinicalObservationVo::getSctidCode));
        clinicalObservationByCode.forEach(this::processData);
    }

    private void processData(String key, List<ClinicalObservationVo> inputValues) {
        LOG.debug("Input parameters -> key {}, RiskFactors {}", key, inputValues);
        inputValues.sort(Comparator.comparing(ClinicalObservationVo::getEffectiveTime).reversed());
        List<ClinicalObservationVo> result = new ArrayList<>();
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

    private Optional<ClinicalObservationVo> getLastNClinicalObservationByCode(String sctidCode, Integer pos){
        List<ClinicalObservationVo> sortedRiskFactor = clinicalObservationByCode.get(sctidCode);
        if (sortedRiskFactor == null || sortedRiskFactor.isEmpty() || sortedRiskFactor.size() <= pos)
            return Optional.empty();
        return Optional.of(sortedRiskFactor.get(pos));
    }

    public List<ClinicalObservationVo> getClinicalObservationByCode(String key){
        return clinicalObservationByCode.getOrDefault(key, new ArrayList<>());
    }

    public Optional<RiskFactorBo> getLastRiskFactors() {
        return getLastNRiskFactors(0);
    }

    public Optional<RiskFactorBo> getLastNRiskFactors(int i) {
        LOG.debug("Input parameters -> pos {}", i);
        RiskFactorBo riskFactorBo = new RiskFactorBo();
        getLastNClinicalObservationByCode(ERiskFactor.BLOOD_OXYGEN_SATURATION.getSctidCode(),i).ifPresent(v ->
            riskFactorBo.setBloodOxygenSaturation(new ClinicalObservationBo(v))
        );
        getLastNClinicalObservationByCode(ERiskFactor.DIASTOLIC_BLOOD_PRESSURE.getSctidCode(),i).ifPresent(v ->
            riskFactorBo.setDiastolicBloodPressure(new ClinicalObservationBo(v))
        );
        getLastNClinicalObservationByCode(ERiskFactor.SYSTOLIC_BLOOD_PRESSURE.getSctidCode(),i).ifPresent(v ->
            riskFactorBo.setSystolicBloodPressure(new ClinicalObservationBo(v))
        );
        getLastNClinicalObservationByCode(ERiskFactor.HEART_RATE.getSctidCode(),i).ifPresent(v ->
            riskFactorBo.setHeartRate(new ClinicalObservationBo(v))
        );
        getLastNClinicalObservationByCode(ERiskFactor.TEMPERATURE.getSctidCode(),i).ifPresent(v ->
            riskFactorBo.setTemperature(new ClinicalObservationBo(v))
        );
        getLastNClinicalObservationByCode(ERiskFactor.RESPIRATORY_RATE.getSctidCode(),i).ifPresent(v ->
            riskFactorBo.setRespiratoryRate(new ClinicalObservationBo(v))
        );
        getLastNClinicalObservationByCode(ERiskFactor.MEAN_PRESSURE.getSctidCode(),i).ifPresent(v ->
            riskFactorBo.setMeanPressure(new ClinicalObservationBo(v))
        );
		getLastNClinicalObservationByCode(ERiskFactor.BLOOD_GLUCOSE.getSctidCode(),i).ifPresent(v ->
				riskFactorBo.setBloodGlucose(new ClinicalObservationBo(v))
		);
		getLastNClinicalObservationByCode(ERiskFactor.GLYCOSYLATED_HEMOGLOBIN.getSctidCode(),i).ifPresent(v ->
				riskFactorBo.setGlycosylatedHemoglobin(new ClinicalObservationBo(v))
		);
		getLastNClinicalObservationByCode(ERiskFactor.CARDIOVASCULAR_RISK.getSctidCode(),i).ifPresent(v ->
				riskFactorBo.setCardiovascularRisk(new ClinicalObservationBo(v))
		);
        LOG.debug(OUTPUT, riskFactorBo);
        if (riskFactorBo.hasValues())
            return Optional.of(riskFactorBo);
        return Optional.empty();
    }

    public Optional<AnthropometricDataBo> getLastAnthropometricData() {
        return getNAnthropometricData(0);
    }

    public Optional<AnthropometricDataBo> getNAnthropometricData(int n) {
        LOG.debug("Input parameters -> pos {}", n);
        AnthropometricDataBo result = new AnthropometricDataBo();
        getLastNClinicalObservationByCode(ERiskFactor.HEIGHT.getSctidCode(),n).ifPresent(v ->
            result.setHeight(new ClinicalObservationBo(v))
        );
        getLastNClinicalObservationByCode(ERiskFactor.WEIGHT.getSctidCode(),n).ifPresent(v ->
            result.setWeight(new ClinicalObservationBo(v))
        );
        getLastNClinicalObservationByCode(EObservationLab.BLOOD_TYPE.getSctidCode(),n).ifPresent(v ->
            result.setBloodType(new ClinicalObservationBo(v))
        );
        LOG.debug(OUTPUT, result);
        if (result.hasValues())
            return Optional.of(result);
        return Optional.empty();
    }

	public List<AnthropometricDataBo> getLastNAnthropometricData(int last){
		LOG.debug("Input parameter -> last {}", last);
		List<AnthropometricDataBo> result = new ArrayList<>();
		for (int i=0; i<last; i++) {
			getNAnthropometricData(i).ifPresent(result::add);
		}
		LOG.debug(OUTPUT, result);
		return result;
	}


}
