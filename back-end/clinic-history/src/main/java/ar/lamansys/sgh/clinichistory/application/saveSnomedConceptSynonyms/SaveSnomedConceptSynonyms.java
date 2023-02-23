package ar.lamansys.sgh.clinichistory.application.saveSnomedConceptSynonyms;

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
public class SaveSnomedConceptSynonyms {

	private final SnomedService snomedService;

	public List<Integer> run (List<SnomedBo> concepts){
		log.debug("Input parameter -> concepts size = {}", concepts.size());
		List<Integer> snomedIds = new ArrayList<>();
		List<SnomedBo> toCreate = new ArrayList<>();
		for (SnomedBo concept : concepts) {
			Integer snomedId = snomedService.getSnomedId(concept).orElse(null);
			if (snomedId == null)
				toCreate.add(concept);
			else
				snomedIds.add(snomedId);
		}
		snomedIds.addAll(snomedService.createSnomedSynonyms(toCreate));
		return snomedIds;
	}

}
