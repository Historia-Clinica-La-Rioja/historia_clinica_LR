package net.pladema.internation.service.domain.ips;

import lombok.Getter;
import net.pladema.internation.repository.ips.generalstate.ClinicalObservationVo;
import net.pladema.internation.service.domain.ips.enums.EObservationLab;
import net.pladema.internation.service.domain.ips.enums.EVitalSign;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.stream.Collectors;

@Getter
public class MapClinicalObservationVo {

    private static final Logger LOG = LoggerFactory.getLogger(MapClinicalObservationVo.class);

    public static final String OUTPUT = "Output -> {}";

    private Map<String, List<ClinicalObservationVo>> clinicalObservationByCode;

    public MapClinicalObservationVo(List<ClinicalObservationVo> vitalsSignVos){
        super();
        clinicalObservationByCode = vitalsSignVos.stream().collect(Collectors.groupingBy(ClinicalObservationVo::getSctidCode));
        clinicalObservationByCode.forEach((k, v) -> processData(k, v));
    }

    private void processData(String key, List<ClinicalObservationVo> inputValues) {
        LOG.debug("Input parameters -> key {}, vitalSigns {}", key, inputValues);
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
        List<ClinicalObservationVo> sortedVitalSign = clinicalObservationByCode.get(sctidCode);
        if (sortedVitalSign == null || sortedVitalSign.isEmpty() || sortedVitalSign.size() <= pos)
            return Optional.empty();
        return Optional.of(sortedVitalSign.get(pos));
    }

    public List<ClinicalObservationVo> getClinicalObservationByCode(String key){
        return clinicalObservationByCode.getOrDefault(key, new ArrayList<>());
    }

    public Optional<VitalSignBo> getLastVitalSigns() {
        return getLastNVitalSigns(0);
    }

    public Optional<VitalSignBo> getLastNVitalSigns(int i) {
        LOG.debug("Input parameters -> pos {}", i);
        VitalSignBo vitalSignBo = new VitalSignBo();
        getLastNClinicalObservationByCode(EVitalSign.BLOOD_OXYGEN_SATURATION.getSctidCode(),i).ifPresent(v -> {
            vitalSignBo.setBloodOxygenSaturation(new ClinicalObservationBo(v));
        });
        getLastNClinicalObservationByCode(EVitalSign.DIASTOLIC_BLOOD_PRESSURE.getSctidCode(),i).ifPresent(v -> {
            vitalSignBo.setDiastolicBloodPressure(new ClinicalObservationBo(v));
        });
        getLastNClinicalObservationByCode(EVitalSign.SYSTOLIC_BLOOD_PRESSURE.getSctidCode(),i).ifPresent(v -> {
            vitalSignBo.setSystolicBloodPressure(new ClinicalObservationBo(v));
        });
        getLastNClinicalObservationByCode(EVitalSign.HEART_RATE.getSctidCode(),i).ifPresent(v -> {
            vitalSignBo.setHeartRate(new ClinicalObservationBo(v));
        });
        getLastNClinicalObservationByCode(EVitalSign.TEMPERATURE.getSctidCode(),i).ifPresent(v -> {
            vitalSignBo.setTemperature(new ClinicalObservationBo(v));
        });
        getLastNClinicalObservationByCode(EVitalSign.RESPIRATORY_RATE.getSctidCode(),i).ifPresent(v -> {
            vitalSignBo.setRespiratoryRate(new ClinicalObservationBo(v));
        });
        getLastNClinicalObservationByCode(EVitalSign.MEAN_PRESSURE.getSctidCode(),i).ifPresent(v -> {
            vitalSignBo.setMeanPressure(new ClinicalObservationBo(v));
        });
        LOG.debug(OUTPUT, vitalSignBo);
        if (vitalSignBo.hasValues())
            return Optional.of(vitalSignBo);
        return Optional.empty();
    }

    public Optional<AnthropometricDataBo> getLastAnthropometricData() {
        return getLastNAnthropometricData(0);
    }

    public Optional<AnthropometricDataBo> getLastNAnthropometricData(int i) {
        LOG.debug("Input parameters -> pos {}", i);
        AnthropometricDataBo result = new AnthropometricDataBo();
        getLastNClinicalObservationByCode(EVitalSign.HEIGHT.getSctidCode(),i).ifPresent(v -> {
            result.setHeight(new ClinicalObservationBo(v));
        });
        getLastNClinicalObservationByCode(EVitalSign.WEIGHT.getSctidCode(),i).ifPresent(v -> {
            result.setWeight(new ClinicalObservationBo(v));
        });
        getLastNClinicalObservationByCode(EObservationLab.BLOOD_TYPE.getSctidCode(),i).ifPresent(v -> {
            result.setBloodType(new ClinicalObservationBo(v));
        });
        LOG.debug(OUTPUT, result);
        if (result.hasValues())
            return Optional.of(result);
        return Optional.empty();
    }

}
