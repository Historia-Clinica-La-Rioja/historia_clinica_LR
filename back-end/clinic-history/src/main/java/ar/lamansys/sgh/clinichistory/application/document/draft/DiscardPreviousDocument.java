package ar.lamansys.sgh.clinichistory.application.document.draft;

import ar.lamansys.sgh.clinichistory.application.document.DocumentService;
import ar.lamansys.sgh.clinichistory.domain.document.IDocumentBo;
import ar.lamansys.sgh.clinichistory.domain.ips.services.ClinicalObservationService;
import ar.lamansys.sgh.clinichistory.domain.ips.services.HealthConditionService;
import ar.lamansys.sgh.clinichistory.domain.ips.services.LoadMedications;
import ar.lamansys.sgh.clinichistory.domain.ips.services.LoadProcedures;
import ar.lamansys.sgh.clinichistory.domain.ips.visitor.DiscardElementIpsVisitor;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.document.DocumentStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class DiscardPreviousDocument {

    private final DocumentService documentService;
    private final HealthConditionService healthConditionService;
    private final ClinicalObservationService clinicalObservationService;
    private final LoadProcedures loadProcedures;
    private final LoadMedications loadMedications;

    public void run(IDocumentBo previousDocument) {
        log.debug("Input parameter -> previousDocument {}", previousDocument);

        this.discardPreviousElements(previousDocument);

        Long previousDocumentId = previousDocument.getId();

        documentService.deleteById(previousDocumentId, DocumentStatus.DRAFT_DISCARDED);

        log.debug("Output -> discarded previous document with id {}", previousDocumentId);
    }

    private void discardPreviousElements(IDocumentBo documentBo) {
        DiscardElementIpsVisitor discardElementIpsVisitor = this.createDiscardElementIpsVisitor(documentBo);
        documentBo.getIpsComponentsWithStatus()
                .forEach(ipsBo -> ipsBo.accept(discardElementIpsVisitor));
    }

    private DiscardElementIpsVisitor createDiscardElementIpsVisitor(IDocumentBo documentBo) {
        return new DiscardElementIpsVisitor(
                healthConditionService,
                clinicalObservationService,
                loadMedications,
                loadProcedures,
                documentBo.getId(),
                documentBo.getPatientId(),
                documentBo.getPatientInfo());
    }
}
