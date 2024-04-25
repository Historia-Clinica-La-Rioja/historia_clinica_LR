package net.pladema.clinichistory.documents.application.draft.factory;

import ar.lamansys.sgh.clinichistory.application.document.draft.CreateDocumentWithDraftSupport;
import ar.lamansys.sgh.clinichistory.application.document.draft.DiscardPreviousDocument;
import ar.lamansys.sgh.clinichistory.application.document.draft.SetNullIdsDocumentElements;
import ar.lamansys.sgh.clinichistory.application.document.draft.factory.DraftSupportFactory;
import lombok.RequiredArgsConstructor;
import net.pladema.clinichistory.hospitalization.application.anestheticreport.CreateAnestheticReportDocument;
import net.pladema.clinichistory.hospitalization.application.anestheticreport.GetAnestheticReport;
import net.pladema.clinichistory.hospitalization.application.port.AnestheticStorage;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class AnestheticReportDraftSupportFactory implements DraftSupportFactory {

    private final DiscardPreviousDocument discardPreviousDocument;
    private final SetNullIdsDocumentElements setNullIdsDocumentElements;
    private final GetAnestheticReport getAnestheticReport;
    private final AnestheticStorage anestheticStorage;
    private final CreateAnestheticReportDocument createAnestheticReportDocument;

    public CreateDocumentWithDraftSupport build() {
        return new CreateDocumentWithDraftSupport(anestheticStorage::getDocumentIdFromLastAnestheticReportDraft,
                getAnestheticReport::run,
                discardPreviousDocument,
                setNullIdsDocumentElements,
                createAnestheticReportDocument::run);
    }
}
