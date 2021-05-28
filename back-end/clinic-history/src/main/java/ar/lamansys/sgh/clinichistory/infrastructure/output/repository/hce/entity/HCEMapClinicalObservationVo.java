package ar.lamansys.sgh.clinichistory.infrastructure.output.repository.hce.entity;

import ar.lamansys.sgh.clinichistory.domain.hce.HCEAnthropometricDataBo;
import ar.lamansys.sgh.clinichistory.domain.hce.HCEClinicalObservationBo;
import ar.lamansys.sgh.clinichistory.domain.hce.HCEVitalSignBo;
import ar.lamansys.sgh.clinichistory.domain.ips.EObservationLab;
import ar.lamansys.sgh.clinichistory.domain.ips.EVitalSign;
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

    public HCEMapClinicalObservationVo(List<HCEClinicalObservationVo> vitalsSignVos){
        super();
        clinicalObservationByCode = vitalsSignVos.stream().collect(Collectors.groupingBy(HCEClinicalObservationVo::getSctidCode));
        clinicalObservationByCode.forEach(this::processData);
    }

    private void processData(String key, List<HCEClinicalObservationVo> inputValues) {
        LOG.debug("Input parameters -> key {}, vitalSigns {}", key, inputValues);
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
        List<HCEClinicalObservationVo> sortedVitalSign = clinicalObservationByCode.get(sctidCode);
        if (sortedVitalSign == null || sortedVitalSign.isEmpty() || sortedVitalSign.size() <= pos)
            return Optional.empty();
        return Optional.of(sortedVitalSign.get(pos));
    }

    public List<HCEClinicalObservationVo> getClinicalObservationByCode(String key){
        return clinicalObservationByCode.getOrDefault(key, new ArrayList<>());
    }

    public Optional<HCEVitalSignBo> getLastVitalSigns() {
        return getLastNVitalSigns(0);
    }

    public Optional<HCEVitalSignBo> getLastNVitalSigns(int i) {
        LOG.debug("Input parameters -> pos {}", i);
        HCEVitalSignBo vitalSignBo = new HCEVitalSignBo();
        getLastNClinicalObservationByCode(EVitalSign.BLOOD_OXYGEN_SATURATION.getSctidCode(),i).ifPresent(v ->
            vitalSignBo.setBloodOxygenSaturation(new HCEClinicalObservationBo(v))
        );
        getLastNClinicalObservationByCode(EVitalSign.DIASTOLIC_BLOOD_PRESSURE.getSctidCode(),i).ifPresent(v -> 
            vitalSignBo.setDiastolicBloodPressure(new HCEClinicalObservationBo(v))
        );
        getLastNClinicalObservationByCode(EVitalSign.SYSTOLIC_BLOOD_PRESSURE.getSctidCode(),i).ifPresent(v -> 
            vitalSignBo.setSystolicBloodPressure(new HCEClinicalObservationBo(v))
        );
        getLastNClinicalObservationByCode(EVitalSign.HEART_RATE.getSctidCode(),i).ifPresent(v -> 
            vitalSignBo.setHeartRate(new HCEClinicalObservationBo(v))
        );
        getLastNClinicalObservationByCode(EVitalSign.TEMPERATURE.getSctidCode(),i).ifPresent(v -> 
            vitalSignBo.setTemperature(new HCEClinicalObservationBo(v))
        );
        getLastNClinicalObservationByCode(EVitalSign.RESPIRATORY_RATE.getSctidCode(),i).ifPresent(v -> 
            vitalSignBo.setRespiratoryRate(new HCEClinicalObservationBo(v))
        );
        getLastNClinicalObservationByCode(EVitalSign.MEAN_PRESSURE.getSctidCode(),i).ifPresent(v -> 
            vitalSignBo.setMeanPressure(new HCEClinicalObservationBo(v))
        );
        LOG.debug(OUTPUT, vitalSignBo);
        if (vitalSignBo.hasValues())
            return Optional.of(vitalSignBo);
        return Optional.empty();
    }

    public Optional<HCEAnthropometricDataBo> getLastAnthropometricData() {
        return getLastNAnthropometricData(0);
    }

    public Optional<HCEAnthropometricDataBo> getLastNAnthropometricData(int i) {
        LOG.debug("Input parameters -> pos {}", i);
        HCEAnthropometricDataBo result = new HCEAnthropometricDataBo();
        getLastNClinicalObservationByCode(EVitalSign.HEIGHT.getSctidCode(),i).ifPresent(v ->
            result.setHeight(new HCEClinicalObservationBo(v))
        );
        getLastNClinicalObservationByCode(EVitalSign.WEIGHT.getSctidCode(),i).ifPresent(v ->
            result.setWeight(new HCEClinicalObservationBo(v))
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
