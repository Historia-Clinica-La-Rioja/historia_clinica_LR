package net.pladema.internation.service.domain.ips;

import lombok.Getter;
import net.pladema.internation.repository.ips.generalstate.VitalSignVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.stream.Collectors;

@Getter
public class MapVitalSigns {

    private static final Logger LOG = LoggerFactory.getLogger(MapVitalSigns.class);

    private Map<String, List<VitalSignVo>> groupByVitalSign;

    public MapVitalSigns(List<VitalSignVo> vitalsSignVos){
        super();
        groupByVitalSign = vitalsSignVos.stream().collect(Collectors.groupingBy(VitalSignVo::getSctidCode));
        groupByVitalSign.forEach((k, v) -> processData(k, v));
    }

    private void processData(String key, List<VitalSignVo> inputValues) {
        LOG.debug("Input parameters -> key {}, vitalSigns {}", key, inputValues);
        inputValues.sort(Comparator.comparing(VitalSignVo::getEffectiveTime).reversed());
        List<VitalSignVo> result = new ArrayList<>();
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
        LOG.debug("Output -> {}", result);
    }

    public Optional<VitalSignVo> getLastNVitalSign(String sctidCode, Integer pos){
        List<VitalSignVo> sortedVitalSign = groupByVitalSign.get(sctidCode);
        if (sortedVitalSign == null || sortedVitalSign.isEmpty() || sortedVitalSign.size() <= pos)
            return Optional.empty();
        return Optional.of(sortedVitalSign.get(pos));
    }

    public List<VitalSignVo> getVitalSignsByCode(String key){
        return groupByVitalSign.getOrDefault(key, new ArrayList<>());
    }
}
