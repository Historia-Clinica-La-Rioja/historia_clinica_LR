package ar.lamansys.sgh.clinichistory.application.document;

import ar.lamansys.sgh.clinichistory.application.ports.DocumentStorage;
import ar.lamansys.sgh.clinichistory.domain.document.DocumentBo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Service
public class FetchDocument {

    private final DocumentStorage documentStorage;

    public Optional<DocumentBo> run(Long documentId) {
        log.debug("Input parameters -> documentId {}", documentId);
        Optional<DocumentBo> result = documentStorage.getDocumentBo(documentId);
        log.debug("Output -> {}", result);
        return result;
    }

}
