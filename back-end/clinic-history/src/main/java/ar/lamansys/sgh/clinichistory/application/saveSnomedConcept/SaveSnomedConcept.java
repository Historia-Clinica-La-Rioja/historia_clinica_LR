package ar.lamansys.sgh.clinichistory.application.saveSnomedConcept;

import ar.lamansys.sgh.clinichistory.domain.ips.SnomedBo;
import ar.lamansys.sgh.clinichistory.domain.ips.services.SnomedService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class SaveSnomedConcept {

    private final SnomedService snomedService;

    public Integer run(SnomedBo concept){
        log.debug("Input parameter -> concept {}", concept);
        Integer result = snomedService.getSnomedId(concept).orElseGet(() -> snomedService.createSnomedTerm(concept));
        log.debug("Output -> {}", result);
        return result;
    }

}
