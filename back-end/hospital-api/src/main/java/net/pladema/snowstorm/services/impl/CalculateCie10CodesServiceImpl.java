package net.pladema.snowstorm.services.impl;

import net.pladema.clinichistory.documents.service.domain.PatientInfoBo;
import net.pladema.snowstorm.services.Cie10RuleChecker;
import net.pladema.snowstorm.services.CalculateCie10CodesService;
import net.pladema.snowstorm.services.SnowstormService;
import net.pladema.snowstorm.services.domain.SnowstormCie10ItemResponse;
import net.pladema.snowstorm.services.domain.SnowstormCie10RefsetMembersResponse;
import org.apache.commons.lang3.StringUtils;
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

    private static final char CODE_DELIMITER = ',';

    private final SnowstormService snowstormService;

    public CalculateCie10CodesServiceImpl(SnowstormService snowstormService) {
        this.snowstormService = snowstormService;
    }

    @Override
    public String execute(String sctid, PatientInfoBo patient) {
        LOG.debug("Input parameters -> sctid {}, patient {}", sctid, patient);
        SnowstormCie10RefsetMembersResponse cie10RefsetMembers = snowstormService.getCie10RefsetMembers(sctid);
        List<String> cie10CodesList = calculateCie10Codes(cie10RefsetMembers, patient);
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

    private List<String> calculateCie10Codes(SnowstormCie10RefsetMembersResponse cie10Response, PatientInfoBo patient) {
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

    private String processRules(List<SnowstormCie10ItemResponse> ruleGroup, PatientInfoBo patient) {
        for (SnowstormCie10ItemResponse rule: ruleGroup){
            if (Cie10RuleChecker.check(rule.getAdditionalFields().getMapRule(), patient))
                return rule.getAdditionalFields().getMapTarget();
        }
        return null;
    }

}
