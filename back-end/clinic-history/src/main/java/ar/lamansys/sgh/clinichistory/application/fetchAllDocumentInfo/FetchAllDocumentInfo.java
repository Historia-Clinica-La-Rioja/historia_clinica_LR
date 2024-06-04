package ar.lamansys.sgh.clinichistory.application.fetchAllDocumentInfo;

import java.util.Optional;

import ar.lamansys.sgh.clinichistory.application.document.FetchDocument;
import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;

import ar.lamansys.sgh.clinichistory.domain.document.DocumentBo;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Service
public class FetchAllDocumentInfo {

    private final FetchDocument fetchDocument;
    private final CompleteInformationForAllDocument completeInformationForAllDocument;

    public Optional<DocumentBo> run(Long id) {
        log.debug("FetchDocumentFile with id {}", id);
        Optional<DocumentBo> result = fetchDocument.run(id);
        result.ifPresent(completeInformationForAllDocument::run);
        log.debug("Output -> {}", result);
        return result;
    }
}
