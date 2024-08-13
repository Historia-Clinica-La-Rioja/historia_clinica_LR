package ar.lamansys.sgh.clinichistory.application.calculatedocumentcreationdate;

import ar.lamansys.sgh.clinichistory.application.ports.DocumentStorage;
import ar.lamansys.sgh.clinichistory.domain.document.DocumentBo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Service
public class CalculateDocumentPerformedDate {

    private final DocumentStorage documentStorage;

    public Optional<LocalDateTime> run(Long initialDocumentId) {
        log.debug("Input parameters -> initialDocumentId {}", initialDocumentId);
        var result = Optional.ofNullable(initialDocumentId)
                .flatMap(documentStorage::getMinimalDocumentBo)
                .map(DocumentBo::getPerformedDate);
        log.debug("Output -> {}", result);
        return result;
    }
}
