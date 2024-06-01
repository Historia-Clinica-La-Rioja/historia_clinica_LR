package ar.lamansys.sgh.clinichistory.application.anestheticreport.visitor;

import ar.lamansys.sgh.clinichistory.application.anestheticreport.SaveAnestheticReportComponents;
import ar.lamansys.sgh.clinichistory.domain.document.impl.AnestheticReportBo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class AnestheticReportVisitorMethods {
    private final SaveAnestheticReportComponents saveAnestheticReportComponents;

    public void saveComponents(AnestheticReportBo anestheticReportBo) {
        saveAnestheticReportComponents.run(anestheticReportBo);
    }

}
