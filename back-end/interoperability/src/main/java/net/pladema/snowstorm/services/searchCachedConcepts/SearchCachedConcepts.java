package net.pladema.snowstorm.services.searchCachedConcepts;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.pladema.snowstorm.repository.SnomedConceptsRepository;
import net.pladema.snowstorm.services.domain.SnomedCachedSearchBo;
import net.pladema.snowstorm.services.domain.semantics.SnomedECL;
import net.pladema.snowstorm.services.domain.semantics.SnomedSemantics;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class SearchCachedConcepts {

    private final SnomedConceptsRepository snomedConceptsRepository;
    private final SnomedSemantics snomedSemantics;

    public List<SnomedCachedSearchBo> run(String term, String eclKey) {
        log.debug("Input parameters -> term {}, eclKey {}", term, eclKey);
        String ecl = snomedSemantics.getEcl(SnomedECL.map(eclKey));
        List<SnomedCachedSearchBo> result = snomedConceptsRepository.searchConceptsByEcl(term, ecl)
                .stream()
                .map(SnomedCachedSearchBo::new)
                .collect(Collectors.toList());
        log.debug("Output -> {}", result);
        return result;
    }

}
