package net.pladema.clinichistory.documents.application.draft;

import ar.lamansys.sgh.clinichistory.application.document.draft.CreateDocumentWithDraftSupport;
import lombok.RequiredArgsConstructor;
import net.pladema.clinichistory.documents.application.draft.factory.AnestheticReportDraftSupportFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@RequiredArgsConstructor
@Configuration
public class DocumentWithDraftSupportConfig {

    private final AnestheticReportDraftSupportFactory anestheticReportDraftSupportFactory;

    @Bean(name = "anesthetic_report")
    public CreateDocumentWithDraftSupport supportAnestheticReport() {
        return anestheticReportDraftSupportFactory.build();
    }
}
