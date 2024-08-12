package ar.lamansys.sgh.clinichistory.application.anestheticreport;

import ar.lamansys.sgh.clinichistory.application.anestheticreport.ports.output.AnestheticReportOutputPort;
import ar.lamansys.sgh.clinichistory.application.document.DeleteDocument;
import ar.lamansys.sgh.clinichistory.application.document.DocumentService;
import ar.lamansys.sgh.clinichistory.application.getdocumentheader.GetDocumentHeader;
import ar.lamansys.sgh.clinichistory.domain.document.DocumentHeaderBo;
import ar.lamansys.sgh.clinichistory.domain.document.enums.EPreviousDocumentStatus;
import ar.lamansys.sgh.clinichistory.domain.document.impl.AnestheticReportBo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@RequiredArgsConstructor
@Service
public class DeleteAnestheticReport {

    private final GetDocumentHeader getDocumentHeader;
    private final AnestheticReportOutputPort anestheticReportOutputPort;
    private final DeleteDocument deleteDocument;
    private final DocumentService documentService;

    @Transactional
    public void run(Long documentId, String reason) {
        log.debug("Input parameters -> documentId {}, reason {}", documentId, reason);

        DocumentHeaderBo documentHeaderBo = (DocumentHeaderBo) getDocumentHeader.run(documentId);
        AnestheticReportBo document = this.mapTo(documentHeaderBo, reason);
        String nextDocumentStatus = EPreviousDocumentStatus.getNext(document);

        anestheticReportOutputPort.validateAnestheticReport(document);
        deleteDocument.run(documentId, nextDocumentStatus);
        documentService.updateDocumentModificationReason(documentId, reason);

        log.debug("Output -> {}", true);
    }

    private AnestheticReportBo mapTo(DocumentHeaderBo documentHeaderBo, String reason) {
        AnestheticReportBo anestheticReportBo = new AnestheticReportBo();
        anestheticReportBo.setId(documentHeaderBo.getId());
        anestheticReportBo.setEncounterId(documentHeaderBo.getEncounterId());
        anestheticReportBo.setPerformedDate(documentHeaderBo.getCreatedOn());
        anestheticReportBo.setModificationReason(reason);
        anestheticReportBo.setCreatedBy(documentHeaderBo.getCreatedBy());
        anestheticReportBo.setDocumentStatusId(documentHeaderBo.getDocumentStatusId());
        return anestheticReportBo;
    }
}
