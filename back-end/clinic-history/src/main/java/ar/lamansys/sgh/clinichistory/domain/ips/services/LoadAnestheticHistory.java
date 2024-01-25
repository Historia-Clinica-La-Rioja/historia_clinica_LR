package ar.lamansys.sgh.clinichistory.domain.ips.services;

import ar.lamansys.sgh.clinichistory.domain.ips.AnestheticHistoryBo;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.document.DocumentAnestheticHistoryRepository;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.document.entity.DocumentAnestheticHistory;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import static java.util.Objects.nonNull;

@Slf4j
@RequiredArgsConstructor
@Service
public class LoadAnestheticHistory {

    private final DocumentAnestheticHistoryRepository documentAnestheticHistoryRepository;

    public AnestheticHistoryBo run(Long documentId, Optional<AnestheticHistoryBo> anestheticHistory) {
        log.debug("Input parameters -> documentId {} anestheticHistory {}", documentId, anestheticHistory);

        anestheticHistory.filter(anestheticHistoryBo -> nonNull(anestheticHistoryBo.getStateId()) || nonNull(anestheticHistoryBo.getZoneId()))
                .ifPresent((anestheticHistoryBo -> {
            Short stateId = anestheticHistoryBo.getStateId();
            Short zoneId = anestheticHistoryBo.getZoneId();
            DocumentAnestheticHistory saved = documentAnestheticHistoryRepository.save(new DocumentAnestheticHistory(documentId, stateId, zoneId));
            anestheticHistoryBo.setId(saved.getDocumentId());
        }));

        log.debug("Output -> {}", anestheticHistory);
        return anestheticHistory.orElse(null);
    }
}
