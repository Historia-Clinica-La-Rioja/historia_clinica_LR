package ar.lamansys.sgh.clinichistory.application.document;

import ar.lamansys.sgh.clinichistory.application.ports.DocumentFileStorage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@RequiredArgsConstructor
@Service
public class DeleteDocument {

    private final DocumentService documentService;
    private final DocumentFileStorage documentFileStorage;

    @Transactional
    public boolean run(Long documentId, String newDocumentStatus) {
        log.debug("Input parameter documentId {}, newDocumentStatus {}", documentId, newDocumentStatus);
        documentService.deleteById(documentId, newDocumentStatus);
        documentFileStorage.deleteById(documentId);
        log.debug("Document {} deleted", documentId);
        return true;
    }
}
