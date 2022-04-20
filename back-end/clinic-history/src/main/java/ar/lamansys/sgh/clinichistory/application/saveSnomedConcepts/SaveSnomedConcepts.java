package ar.lamansys.sgh.clinichistory.application.saveSnomedConcepts;

import ar.lamansys.sgh.clinichistory.domain.ips.SnomedBo;
import ar.lamansys.sgh.clinichistory.domain.ips.services.SnomedService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class SaveSnomedConcepts {

    private final SnomedService snomedService;

    public List<Integer> run(List<SnomedBo> concepts) {
		log.debug("Input parameter -> concepts size = {}", concepts.size());
        List<Integer> snomedIds = new ArrayList<>();
        List<SnomedBo> conceptsToCreate = new ArrayList<>();
        for (SnomedBo concept : concepts) {
            Integer snomedId = snomedService.getSnomedId(concept).orElse(null);
            if (snomedId != null)
                snomedIds.add(snomedId);
            else
                conceptsToCreate.add(concept);
        }
        snomedIds.addAll(snomedService.createSnomedTerms(conceptsToCreate));
        return snomedIds;
    }

}
