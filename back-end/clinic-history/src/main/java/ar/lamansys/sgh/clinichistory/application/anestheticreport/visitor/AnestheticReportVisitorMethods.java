package ar.lamansys.sgh.clinichistory.application.anestheticreport.visitor;

import ar.lamansys.sgh.clinichistory.application.anestheticreport.SaveAnestheticReportComponents;
import ar.lamansys.sgh.clinichistory.application.anestheticreport.GenerateAnestheticReportDocumentContext;
import ar.lamansys.sgh.clinichistory.domain.document.impl.AnestheticReportBo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Map;

@RequiredArgsConstructor
@Component
public class AnestheticReportVisitorMethods {

    private final GenerateAnestheticReportDocumentContext generateAnestheticReportDocumentContext;
    private final SaveAnestheticReportComponents saveAnestheticReportComponents;

    public void saveComponents(AnestheticReportBo anestheticReportBo) {
        saveAnestheticReportComponents.run(anestheticReportBo);
    }

    public Map<String, Object> generateContext(AnestheticReportBo anestheticReportBo) {
        return generateAnestheticReportDocumentContext.run(anestheticReportBo);
    }

}
