package ar.lamansys.sgh.clinichistory.application.anestheticreport.draft;

import ar.lamansys.sgh.clinichistory.application.anestheticreport.CreateAnestheticReportDocument;
import ar.lamansys.sgh.clinichistory.application.anestheticreport.ports.AnestheticReportStorage;
import ar.lamansys.sgh.clinichistory.application.document.draft.CreateDocumentWithDraftSupport;
import ar.lamansys.sgh.clinichistory.application.document.draft.DiscardPreviousDocument;
import ar.lamansys.sgh.clinichistory.domain.ips.visitor.SetNullIdsIpsVisitor;
import ar.lamansys.sgh.clinichistory.application.document.draft.factory.DraftSupportFactory;
import ar.lamansys.sgh.shared.infrastructure.input.service.SharedPatientPort;
import lombok.RequiredArgsConstructor;
import ar.lamansys.sgh.clinichistory.application.anestheticreport.GetAnestheticReport;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class AnestheticReportDraftSupportFactory implements DraftSupportFactory {

    private final DiscardPreviousDocument discardPreviousDocument;
    private final SetNullIdsIpsVisitor setNullIdsIpsVisitor;
    private final GetAnestheticReport getAnestheticReport;
    private final AnestheticReportStorage anestheticReportStorage;
    private final CreateAnestheticReportDocument createAnestheticReportDocument;
    private final SharedPatientPort sharedPatientPort;

    public CreateDocumentWithDraftSupport build() {
        return new CreateDocumentWithDraftSupport(anestheticReportStorage::getDocumentIdFromLastAnestheticReportDraft,
                getAnestheticReport::run,
                discardPreviousDocument,
                setNullIdsIpsVisitor,
                createAnestheticReportDocument::run,
                sharedPatientPort::getBasicDataFromPatient);
    }
}
