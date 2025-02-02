package net.pladema.snowstorm.services.searchCachedConcepts;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.pladema.snowstorm.domain.SnomedSearchItemBo;
import net.pladema.snowstorm.repository.SnomedConceptsRepository;
import net.pladema.snowstorm.repository.domain.SnomedSearchVo;
import net.pladema.snowstorm.services.domain.SnomedSearchBo;
import net.pladema.snowstorm.services.domain.semantics.SnomedECL;
import net.pladema.snowstorm.services.domain.semantics.SnomedSemantics;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class SearchCachedConceptsWithResultCountAndNoTerm {

    private final SnomedConceptsRepository snomedConceptsRepository;
    private final SnomedSemantics snomedSemantics;

    public SnomedSearchBo run(String eclKey) {
        log.debug("Input parameters -> eclKey {}", eclKey);
        String ecl = snomedSemantics.getEcl(SnomedECL.map(eclKey));
        SnomedSearchVo searchResult = snomedConceptsRepository.searchConceptsWithResultCountByEcl(null, ecl, eclKey);
        List<SnomedSearchItemBo> items = searchResult.getItems()
                .stream()
                .map(SnomedSearchItemBo::new)
                .collect(Collectors.toList());
        SnomedSearchBo result = new SnomedSearchBo(items, searchResult.getTotalMatches());
        log.debug("Output -> {}", result);
        return result;
    }

}
