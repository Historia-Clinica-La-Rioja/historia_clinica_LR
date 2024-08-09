package ar.lamansys.sgh.clinichistory.application.anestheticreport;

import ar.lamansys.sgh.clinichistory.application.anestheticreport.ports.AnestheticReportStorage;
import ar.lamansys.sgh.clinichistory.application.getdocumentheader.GetDocumentHeader;
import ar.lamansys.sgh.clinichistory.domain.document.DocumentHeaderBo;
import ar.lamansys.sgh.clinichistory.domain.document.IEditableDocumentBo;
import ar.lamansys.sgh.clinichistory.domain.document.enums.EPreviousDocumentStatus;
import ar.lamansys.sgh.clinichistory.domain.document.impl.AnestheticReportBo;
import ar.lamansys.sgh.shared.infrastructure.input.service.SharedDocumentPort;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@RequiredArgsConstructor
@Service
public class DeleteAnestheticReport {

    private final GetDocumentHeader getDocumentHeader;
    private final AnestheticReportStorage anestheticReportStorage;
    private final SharedDocumentPort sharedDocumentPort;

    @Transactional
    public void run(Long documentId, String reason) {
        log.debug("Input parameters -> documentId {}", documentId);

        DocumentHeaderBo documentHeaderBo = (DocumentHeaderBo) getDocumentHeader.run(documentId);

        anestheticReportStorage.validateAnestheticReport(documentId, reason);
        AnestheticReportBo document = this.mapTo(documentHeaderBo, reason);
        String nextDocumentStatus = EPreviousDocumentStatus.getNext(document);
        sharedDocumentPort.deleteDocument(documentId, nextDocumentStatus);
        sharedDocumentPort.updateDocumentModificationReason(documentId, reason);

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
