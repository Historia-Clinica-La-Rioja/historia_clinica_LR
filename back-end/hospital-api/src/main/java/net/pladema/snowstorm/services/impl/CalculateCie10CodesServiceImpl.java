package net.pladema.snowstorm.services.impl;

import net.pladema.patient.controller.dto.BasicPatientDto;
import net.pladema.snowstorm.services.Cie10RuleChecker;
import net.pladema.snowstorm.services.CalculateCie10CodesService;
import net.pladema.snowstorm.services.SnowstormService;
import net.pladema.snowstorm.services.domain.SnowstormCie10ItemResponse;
import net.pladema.snowstorm.services.domain.SnowstormCie10RefsetMembersResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class CalculateCie10CodesServiceImpl implements CalculateCie10CodesService {

    private static final Logger LOG = LoggerFactory.getLogger(CalculateCie10CodesServiceImpl.class);
    public static final String OUTPUT = "Output -> {}";

    private final SnowstormService snowstormService;

    public CalculateCie10CodesServiceImpl(SnowstormService snowstormService) {
        this.snowstormService = snowstormService;
    }

    @Override
    public List<String> execute(String snomedCode, BasicPatientDto patient) {
        LOG.debug("Input parameters -> snomedCode {}, patient {}", snomedCode, patient);
        SnowstormCie10RefsetMembersResponse cie10RefsetMembers = snowstormService.getCie10RefsetMembers(snomedCode);
        List<String> cie10Codes = calculateCie10Codes(cie10RefsetMembers, patient);
        LOG.debug(OUTPUT, cie10Codes);
        return cie10Codes;
    }

    private List<String> calculateCie10Codes(SnowstormCie10RefsetMembersResponse cie10Response, BasicPatientDto patient) {
        LOG.debug("Input parameters -> cie10Response {}, patient {}", cie10Response, patient);
        Map<String, List<SnowstormCie10ItemResponse>> groupedItems = groupByMapGroupSortedByPriority(cie10Response.getItems());
        List<String> result = groupedItems.values()
                .stream()
                .map(ruleGroup -> processRules(ruleGroup, patient))
                .filter(Objects::nonNull)
                .filter(resultingCie10 -> !resultingCie10.isEmpty())
                .collect(Collectors.toList());
        LOG.debug("Result size = {}", result.size());
        LOG.trace(OUTPUT, result);
        return result;
    }

    private Map<String, List<SnowstormCie10ItemResponse>> groupByMapGroupSortedByPriority(List<SnowstormCie10ItemResponse> items) {
        LOG.debug("Input parameter -> items size = {}", items.size());
        LOG.trace("Input parameter -> items {}", items);
        Map<String, List<SnowstormCie10ItemResponse>> result = items.stream()
                .sorted(Comparator.comparing(i -> Integer.valueOf(i.getAdditionalFields().getMapPriority())))
                .collect(Collectors.groupingBy(item -> item.getAdditionalFields().getMapGroup()));
        LOG.debug(OUTPUT, result);
        LOG.trace("Output -> result key set = {}", result.keySet());
        return result;
    }

    private String processRules(List<SnowstormCie10ItemResponse> ruleGroup, BasicPatientDto patient) {
        for (SnowstormCie10ItemResponse rule: ruleGroup){
            if (Cie10RuleChecker.check(rule.getAdditionalFields().getMapRule(), patient))
                return rule.getAdditionalFields().getMapTarget();
        }
        return null;
    }

}
