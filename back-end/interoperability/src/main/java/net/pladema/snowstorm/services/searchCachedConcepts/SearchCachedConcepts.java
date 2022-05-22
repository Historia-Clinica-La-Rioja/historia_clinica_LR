package net.pladema.snowstorm.services.searchCachedConcepts;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.pladema.snowstorm.repository.SnomedConceptsRepository;
import net.pladema.snowstorm.repository.domain.SnomedSearchVo;
import net.pladema.snowstorm.services.domain.SnomedSearchBo;
import net.pladema.snowstorm.services.domain.SnomedSearchItemBo;
import net.pladema.snowstorm.services.domain.semantics.SnomedECL;
import net.pladema.snowstorm.services.domain.semantics.SnomedSemantics;

@Service
@RequiredArgsConstructor
@Slf4j
public class SearchCachedConcepts {

    private final SnomedConceptsRepository snomedConceptsRepository;
    private final SnomedSemantics snomedSemantics;

    public SnomedSearchBo run(String term, String eclKey) {
        log.debug("Input parameters -> term {}, eclKey {}", term, eclKey);
        String ecl = snomedSemantics.getEcl(SnomedECL.map(eclKey));
        SnomedSearchVo searchResult = snomedConceptsRepository.searchConceptsByEcl(term, ecl, eclKey);
        List<SnomedSearchItemBo> items = searchResult.getItems()
                .stream()
                .map(SnomedSearchItemBo::new)
                .collect(Collectors.toList());
        SnomedSearchBo result = new SnomedSearchBo(items, searchResult.getTotalMatches());
        log.debug("Output -> {}", result);
        return result;
    }

}
