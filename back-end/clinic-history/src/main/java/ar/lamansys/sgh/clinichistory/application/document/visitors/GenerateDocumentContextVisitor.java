package ar.lamansys.sgh.clinichistory.application.document.visitors;

import ar.lamansys.sgh.clinichistory.application.anestheticreport.visitor.AnestheticReportVisitorMethods;
import ar.lamansys.sgh.clinichistory.domain.document.visitor.DocumentVisitor;
import ar.lamansys.sgh.clinichistory.domain.document.impl.AnestheticReportBo;
import ar.lamansys.sgh.clinichistory.domain.document.IDocumentBo;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.document.generateFile.AuditableContextBuilder;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Map;

@RequiredArgsConstructor
@Service
public class GenerateDocumentContextVisitor implements DocumentVisitor {

    private final AnestheticReportVisitorMethods anestheticReportVisitorMethods;
    private final AuditableContextBuilder auditableContextBuilder;

    @Override
    public void visitAnestheticReport(AnestheticReportBo anestheticReportBo) {
        Map<String,Object> contextMap = anestheticReportVisitorMethods.generateContext(anestheticReportBo);
        anestheticReportBo.setContextMap(contextMap);
    }

    @Override
    public void visit(IDocumentBo documentBo) {
        Map<String,Object> contextMap = auditableContextBuilder.buildContext(documentBo);
        documentBo.setContextMap(contextMap);
    }
}
