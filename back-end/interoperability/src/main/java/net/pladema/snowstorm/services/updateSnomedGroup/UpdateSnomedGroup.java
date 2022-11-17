package net.pladema.snowstorm.services.updateSnomedGroup;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import ar.lamansys.sgh.shared.infrastructure.input.service.SharedSnomedDto;
import ar.lamansys.sgh.shared.infrastructure.input.service.SharedSnomedPort;
import ar.lamansys.sgx.shared.dates.configuration.DateTimeProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.pladema.snowstorm.repository.SnomedGroupRepository;
import net.pladema.snowstorm.repository.SnomedRelatedGroupRepository;
import net.pladema.snowstorm.repository.entity.SnomedGroup;
import net.pladema.snowstorm.repository.entity.SnomedGroupType;
import net.pladema.snowstorm.repository.entity.SnomedRelatedGroup;
import net.pladema.snowstorm.services.SnowstormService;
import net.pladema.snowstorm.services.domain.SnowstormSearchResponse;
import net.pladema.snowstorm.services.domain.semantics.SnomedECL;
import net.pladema.snowstorm.services.domain.semantics.SnomedSemantics;
import net.pladema.snowstorm.services.exceptions.SnowstormApiException;

@Service
@RequiredArgsConstructor
@Slf4j
public class UpdateSnomedGroup {

    private final SnowstormService snowstormService;
    private final SnomedGroupRepository snomedGroupRepository;
    private final SharedSnomedPort sharedSnomedPort;
    private final SnomedRelatedGroupRepository snomedRelatedGroupRepository;
    private final SnomedSemantics snomedSemantics;
    private final DateTimeProvider dateTimeProvider;


    public void run(String eclKey) throws SnowstormApiException {
        log.debug("Input parameter -> eclKey {}", eclKey);
        Integer conceptsProcessed = 1;
        String searchAfter = null;
        LocalDate today = dateTimeProvider.nowDate();

        Integer snomedGroupId = saveSnomedGroup(eclKey, today);
        do {
            SnowstormSearchResponse response = snowstormService.getConceptsByEclKey(eclKey, searchAfter);
            searchAfter = response.getSearchAfter();
            List<Integer> conceptIds = sharedSnomedPort.addSnomedConcepts(mapToDto(response));
            conceptsProcessed = associateConceptIdsWithSnomedGroup(snomedGroupId, conceptIds, conceptsProcessed, today);
            log.trace("Concepts processed -> {}", conceptsProcessed);
        }
        while (searchAfter != null);
        log.debug("Finished updating Snomed group");
    }

    private List<SharedSnomedDto> mapToDto(SnowstormSearchResponse response) {
        return response.getItems().stream()
                .map(i -> new SharedSnomedDto(i.getConceptId(),
						i.getPt().get("term").asText(),
						null,
						null))
                .collect(Collectors.toList());
    }

    private Integer associateConceptIdsWithSnomedGroup(Integer snomedGroupId, List<Integer> conceptIds, Integer orden, LocalDate date) {
        for (Integer snomedId : conceptIds) {
			SnomedRelatedGroup snomedRelatedGroup = snomedRelatedGroupRepository.getByGroupIdAndSnomedId(snomedGroupId, snomedId)
					.orElse(new SnomedRelatedGroup(snomedId, snomedGroupId));
            snomedRelatedGroup.setOrden(orden);
			snomedRelatedGroup.setLastUpdate(date);
            snomedRelatedGroupRepository.save(snomedRelatedGroup);
            orden = orden + 1;
        }
        return orden;
    }

    private Integer saveSnomedGroup(String eclKey, LocalDate date) {
        String ecl = snomedSemantics.getEcl(SnomedECL.map(eclKey));
        Integer snomedGroupId = snomedGroupRepository.getBaseGroupIdByEclAndDescription(ecl, eclKey);
        String customId = "1";

        SnomedGroup toSave = new SnomedGroup(snomedGroupId, eclKey, ecl, customId, date, SnomedGroupType.DEFAULT);
        return snomedGroupRepository.save(toSave).getId();
    }

}
