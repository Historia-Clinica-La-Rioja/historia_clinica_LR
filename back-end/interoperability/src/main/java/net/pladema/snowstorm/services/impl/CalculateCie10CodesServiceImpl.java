package net.pladema.snowstorm.services.impl;

import net.pladema.snowstorm.services.CalculateCie10CodesService;
import net.pladema.snowstorm.services.Cie10RuleChecker;
import net.pladema.snowstorm.services.SnowstormService;
import net.pladema.snowstorm.services.domain.Cie10RuleFeature;
import net.pladema.snowstorm.services.domain.SnowstormCie10ItemResponse;
import net.pladema.snowstorm.services.domain.SnowstormCie10RefsetMembersResponse;
import net.pladema.snowstorm.services.exceptions.SnowstormApiException;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class CalculateCie10CodesServiceImpl implements CalculateCie10CodesService {

    private static final Logger LOG = LoggerFactory.getLogger(CalculateCie10CodesServiceImpl.class);
    public static final String OUTPUT = "Output -> {}";

    private static final char CODE_DELIMITER = ',';

    public static final String CIE10_REFERENCE_SET_ID = "447562003";

    public static final String CIE10_LIMIT = "500";

    private final SnowstormService snowstormService;

    public CalculateCie10CodesServiceImpl(SnowstormService snowstormService) {
        this.snowstormService = snowstormService;
    }

    @Override
    public String execute(String sctid, Cie10RuleFeature features) throws SnowstormApiException {
        LOG.debug("Input parameters -> sctid {}, patient {}", sctid, features);
        SnowstormCie10RefsetMembersResponse cie10RefsetMembers =
                snowstormService.getRefsetMembers(sctid, CIE10_REFERENCE_SET_ID, CIE10_LIMIT, SnowstormCie10RefsetMembersResponse.class);
        List<String> cie10CodesList = calculateCie10Codes(cie10RefsetMembers, features);
        String cie10Codes = cie10CodesList.isEmpty() ? null : concatCodes(cie10CodesList);
        LOG.debug(OUTPUT, cie10Codes);
        return cie10Codes;
    }

    private String concatCodes(List<String> cie10CodesList) {
        LOG.debug("Input parameter -> cie10CodesList size = {}", cie10CodesList.size());
        LOG.trace("Input parameter -> cie10CodesList {}", cie10CodesList);
        String result = StringUtils.join(cie10CodesList, CODE_DELIMITER);
        LOG.debug(OUTPUT, result);
        return result;
    }

    private List<String> calculateCie10Codes(SnowstormCie10RefsetMembersResponse cie10Response, Cie10RuleFeature features) {
        LOG.debug("Input parameters -> cie10Response {}, patient {}", cie10Response, features);
        Map<String, List<SnowstormCie10ItemResponse>> groupedItems = groupByMapGroupSortedByPriority(cie10Response != null ? cie10Response.getItems() : Collections.emptyList());
        List<String> result = groupedItems.values()
                .stream()
                .map(ruleGroup -> processRules(ruleGroup, features))
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

    private String processRules(List<SnowstormCie10ItemResponse> ruleGroup, Cie10RuleFeature features) {
        for (SnowstormCie10ItemResponse rule: ruleGroup){
            if (Cie10RuleChecker.check(rule.getAdditionalFields().getMapRule(), features))
                return rule.getAdditionalFields().getMapTarget();
        }
        return null;
    }

}
