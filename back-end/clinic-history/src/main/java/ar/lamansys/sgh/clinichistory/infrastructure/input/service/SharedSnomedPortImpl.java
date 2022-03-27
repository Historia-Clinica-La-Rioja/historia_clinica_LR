package ar.lamansys.sgh.clinichistory.infrastructure.input.service;


import ar.lamansys.sgh.clinichistory.application.saveSnomedConcept.SaveSnomedConcept;
import ar.lamansys.sgh.clinichistory.domain.ips.SnomedBo;
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

    private final SaveSnomedConcept saveSnomedConcept;

    @Override
    public List<Integer> addSnomedConcepts(List<SharedSnomedDto> concepts) {
        log.debug("Input parameter -> concepts {}", concepts);
        List<Integer> result = concepts.stream()
                .map(this::mapToSnomedBo)
                .map(saveSnomedConcept::run)
                .collect(Collectors.toList());
        log.debug("Output -> {}", result);
        return result;
    }

    private SnomedBo mapToSnomedBo(SharedSnomedDto snomedDto) {
        return new SnomedBo(null,
                snomedDto.getSctid(),
                snomedDto.getPt(),
                snomedDto.getParentId(),
                snomedDto.getParentFsn());
    }

}

