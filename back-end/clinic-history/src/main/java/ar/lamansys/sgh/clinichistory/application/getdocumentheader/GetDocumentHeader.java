package ar.lamansys.sgh.clinichistory.application.getdocumentheader;

import ar.lamansys.sgh.clinichistory.application.document.DocumentService;
import ar.lamansys.sgh.clinichistory.application.ports.DocumentStorage;
import ar.lamansys.sgh.clinichistory.domain.document.DocumentHeaderBo;
import ar.lamansys.sgh.clinichistory.domain.document.IDocumentHeaderBo;
import ar.lamansys.sgh.clinichistory.domain.document.SourceTypeBo;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class GetDocumentHeader {

    private final DocumentService documentService;
    private final DocumentStorage documentStorage;

    public IDocumentHeaderBo run(Long documentId) {
        log.debug("Input parameters -> documentId {}", documentId);
        DocumentHeaderBo result = new DocumentHeaderBo();

        documentService.findById(documentId)
                .ifPresent((document -> {
                    result.setId(documentId);
                    result.setEncounterId(document.getSourceId());
                    result.setInstitutionId(document.getInstitutionId());
                    result.setCreatedOn(document.getCreatedOn());
                    result.setCreatedBy(document.getCreatedBy());
                    result.setSourceType(new SourceTypeBo(document.getSourceTypeId(), null));
                    result.setClinicalSpecialtyId(document.getClinicalSpecialtyId());
                    result.setInitialDocumentId(document.getInitialDocumentId());
                }));

        this.setFirstCreatedOn(result);

        log.debug("Output -> {}", result);
        return result;
    }

    private void setFirstCreatedOn(DocumentHeaderBo result) {
        Optional.ofNullable(result.getInitialDocumentId())
                .flatMap(documentStorage::getMinimalDocumentBo)
                .ifPresent(documentBo -> result.setCreatedOn(documentBo.getPerformedDate()));
    }
}
