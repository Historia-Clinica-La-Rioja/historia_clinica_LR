package ar.lamansys.sgh.clinichistory.application.document.edition;

import ar.lamansys.sgh.clinichistory.application.document.DocumentService;
import ar.lamansys.sgh.clinichistory.domain.document.IEditableDocumentBo;
import ar.lamansys.sgh.clinichistory.domain.document.enums.EPreviousDocumentStatus;
import ar.lamansys.sgh.clinichistory.domain.ips.services.ClinicalObservationService;
import ar.lamansys.sgh.clinichistory.domain.ips.services.HealthConditionService;
import ar.lamansys.sgh.clinichistory.domain.ips.services.LoadMedication;
import ar.lamansys.sgh.clinichistory.domain.ips.services.LoadProcedure;
import ar.lamansys.sgh.clinichistory.domain.ips.visitor.DiscardElementIpsVisitor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@RequiredArgsConstructor
@Service
public class UpdatePreviousDocument {

    private final DocumentService documentService;
    private final HealthConditionService healthConditionService;
    private final ClinicalObservationService clinicalObservationService;
    private final LoadProcedure loadProcedure;
    private final LoadMedication loadMedication;

    @Transactional
    public void run(IEditableDocumentBo previousDocument) {
        log.debug("Input parameter -> previousDocument {}", previousDocument);

        this.discardPreviousElements(previousDocument);

        String nextPreviousDocumentStatus = EPreviousDocumentStatus.getNext(previousDocument);

        if (this.isEditedAfterConfirmed(nextPreviousDocumentStatus)) {
            this.saveModificationReason(previousDocument);
        }
        Long previousDocumentId = previousDocument.getId();
        documentService.deleteById(previousDocumentId, nextPreviousDocumentStatus);

        log.debug("Output -> discarded previous document with id {}", previousDocumentId);
    }

    private boolean isEditedAfterConfirmed(String nextDocumentStatus) {
        return EPreviousDocumentStatus.FINAL.getNext()
                .getDescription()
                .equals(nextDocumentStatus);
    }

    private void discardPreviousElements(IEditableDocumentBo documentBo) {
        DiscardElementIpsVisitor discardElementIpsVisitor = this.createDiscardElementIpsVisitor(documentBo);
        documentBo.getIpsComponentsWithStatus()
                .forEach(ipsBo -> ipsBo.accept(discardElementIpsVisitor));
    }

    private DiscardElementIpsVisitor createDiscardElementIpsVisitor(IEditableDocumentBo documentBo) {
        return new DiscardElementIpsVisitor(
                healthConditionService,
                clinicalObservationService,
                loadMedication,
                loadProcedure,
                documentBo.getId(),
                documentBo.getPatientId(),
                documentBo.getPatientInfo());
    }

    private void saveModificationReason(IEditableDocumentBo previousDocument) {
        Long previousDocumentId = previousDocument.getId();
        documentService.updateDocumentModificationReason(previousDocumentId, previousDocument.getModificationReason());
    }
}
