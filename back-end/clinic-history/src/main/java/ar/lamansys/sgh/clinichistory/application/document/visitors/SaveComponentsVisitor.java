package ar.lamansys.sgh.clinichistory.application.document.visitors;

import ar.lamansys.sgh.clinichistory.application.anestheticreport.visitor.AnestheticReportVisitorMethods;
import ar.lamansys.sgh.clinichistory.domain.document.visitor.DocumentVisitor;
import ar.lamansys.sgh.clinichistory.domain.document.impl.AnestheticReportBo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service(value = "save_components_visitor")
public class SaveComponentsVisitor implements DocumentVisitor {

    private final AnestheticReportVisitorMethods anestheticReportVisitorMethods;
    
    @Override
    public void visitAnestheticReport(AnestheticReportBo documentBo) {
        anestheticReportVisitorMethods.saveComponents(documentBo);
    }
}
