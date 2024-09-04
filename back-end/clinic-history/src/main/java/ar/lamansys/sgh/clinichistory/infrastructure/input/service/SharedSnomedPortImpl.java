package ar.lamansys.sgh.clinichistory.infrastructure.input.service;


import ar.lamansys.sgh.clinichistory.application.saveSnomedConceptSynonyms.SaveSnomedConceptSynonyms;
import ar.lamansys.sgh.clinichistory.application.saveSnomedConcepts.SaveSnomedConcepts;
import ar.lamansys.sgh.clinichistory.domain.ips.SnomedBo;
import ar.lamansys.sgh.clinichistory.domain.ips.services.SnomedService;
import ar.lamansys.sgh.shared.infrastructure.input.service.SharedSnomedDto;
import ar.lamansys.sgh.shared.infrastructure.input.service.SharedSnomedPort;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class SharedSnomedPortImpl implements SharedSnomedPort {

    private final SaveSnomedConcepts saveSnomedConcept;

	private final SaveSnomedConceptSynonyms saveSnomedConceptSynonyms;
	private final SnomedService snomedService;

    @Override
    public List<Integer> addSnomedConcepts(List<SharedSnomedDto> concepts) {
        log.debug("Input parameter -> concepts size {}", concepts.size());
        log.trace("Input parameter -> concepts {}", concepts);
        List<Integer> result = saveSnomedConcept.run(mapToSnomedBoList(concepts));
        log.debug("Output size -> {}", result.size());
        log.trace("Output -> {}", result);
        return result;
    }

	@Override
	public List<Integer> addSnomedSynonyms(List<SharedSnomedDto> synonyms) {
		log.debug("Input parameter -> concepts size {}", synonyms.size());
		log.trace("Input parameter -> concepts {}", synonyms);
		List<Integer> result = saveSnomedConceptSynonyms.run(mapToSnomedBoList(synonyms));
		log.debug("Output size -> {}", result.size());
		log.trace("Output -> {}", result);
		return result;
	}

	@Override
	public SharedSnomedDto getSnomed(Integer id) {
		log.debug("Input parameter -> snomed id {}", id);
		SnomedBo result = snomedService.getSnomed(id);
		log.debug("Output -> {}", result);
		return new SharedSnomedDto(result.getSctid(), result.getPt(), result.getParentId(),result.getParentFsn());
	}

	private List<SnomedBo> mapToSnomedBoList(List<SharedSnomedDto> concepts) {
        return concepts.stream()
                .map(this::mapToSnomedBo)
                .collect(Collectors.toList());
    }

    private SnomedBo mapToSnomedBo(SharedSnomedDto snomedDto) {
        return new SnomedBo(null,
                snomedDto.getSctid(),
                snomedDto.getPt(),
                snomedDto.getParentId(),
                snomedDto.getParentFsn(),
				false);
    }

}

