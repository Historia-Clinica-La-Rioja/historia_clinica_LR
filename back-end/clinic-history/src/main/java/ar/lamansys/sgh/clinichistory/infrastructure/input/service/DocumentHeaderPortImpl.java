package ar.lamansys.sgh.clinichistory.infrastructure.input.service;

import ar.lamansys.sgh.clinichistory.application.getdocumentheader.GetDocumentHeader;
import ar.lamansys.sgh.clinichistory.domain.document.IDocumentHeaderBo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class DocumentHeaderPortImpl implements DocumentHeaderPort {

    private final GetDocumentHeader getDocumentHeader;

    @Override
    public IDocumentHeaderBo getDocumentHeader(Long documentId) {
        log.debug("Input parameters -> documentId {}", documentId);
        var result = getDocumentHeader.run(documentId);
        log.debug("Ouput -> {}", result);
        return result;
    }
}
