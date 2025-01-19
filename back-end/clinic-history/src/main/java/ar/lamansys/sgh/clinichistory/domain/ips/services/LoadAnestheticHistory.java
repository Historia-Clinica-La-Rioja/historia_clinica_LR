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

        anestheticHistory.filter(this::hasToSaveEntity)
                .ifPresent(anestheticHistoryBo -> saveEntity(documentId, anestheticHistoryBo));

        log.debug("Output -> {}", anestheticHistory);
        return anestheticHistory.orElse(null);
    }

    private void saveEntity(Long documentId, AnestheticHistoryBo anestheticHistoryBo) {
        Short stateId = anestheticHistoryBo.getStateId();
        Short zoneId = anestheticHistoryBo.getZoneId();
        DocumentAnestheticHistory saved = documentAnestheticHistoryRepository.save(new DocumentAnestheticHistory(documentId, stateId, zoneId));
        anestheticHistoryBo.setId(saved.getDocumentId());
    }

    private boolean hasToSaveEntity(AnestheticHistoryBo anestheticHistoryBo) {
        return nonNull(anestheticHistoryBo.getStateId()) || nonNull(anestheticHistoryBo.getZoneId());
    }
}
