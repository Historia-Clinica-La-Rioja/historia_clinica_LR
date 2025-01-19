package ar.lamansys.sgh.clinichistory.application.getdocumentheader;

import ar.lamansys.sgh.clinichistory.application.calculatedocumentcreationdate.CalculateDocumentPerformedDate;
import ar.lamansys.sgh.clinichistory.application.document.FetchDocument;
import ar.lamansys.sgh.clinichistory.domain.document.DocumentBo;
import ar.lamansys.sgh.clinichistory.domain.document.DocumentHeaderBo;
import ar.lamansys.sgh.clinichistory.domain.document.IDocumentHeaderBo;
import ar.lamansys.sgh.clinichistory.domain.document.SourceTypeBo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class GetDocumentHeader {

    private final FetchDocument fetchDocument;
    private final CalculateDocumentPerformedDate calculateDocumentPerformedDate;

    public IDocumentHeaderBo run(Long documentId) {
        log.debug("Input parameters -> documentId {}", documentId);
        DocumentHeaderBo result = new DocumentHeaderBo();

        fetchDocument.run(documentId)
                .ifPresent(document -> completeInformation(documentId, document, result));

        log.debug("Output -> {}", result);
        return result;
    }

    private void completeInformation(Long documentId, DocumentBo document, DocumentHeaderBo result) {
        result.setId(documentId);
        result.setEncounterId(document.getEncounterId());
        result.setInstitutionId(document.getInstitutionId());
        result.setCreatedOn(document.getPerformedDate());
        result.setCreatedBy(document.getCreatedBy());
        result.setSourceType(new SourceTypeBo(document.getDocumentSource(), null));
        result.setClinicalSpecialtyId(document.getClinicalSpecialtyId());
        result.setInitialDocumentId(document.getInitialDocumentId());
        result.setDocumentStatusId(document.getDocumentStatusId());

        calculateDocumentPerformedDate.run(result.getInitialDocumentId())
                .ifPresent(result::setCreatedOn);
    }
}
