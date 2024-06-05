package ar.lamansys.sgh.clinichistory.application.document.draft.factory;

import ar.lamansys.sgh.clinichistory.application.document.draft.CreateDocumentWithDraftSupport;
import lombok.RequiredArgsConstructor;
import ar.lamansys.sgh.clinichistory.application.anestheticreport.draft.AnestheticReportDraftSupportFactory;
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
