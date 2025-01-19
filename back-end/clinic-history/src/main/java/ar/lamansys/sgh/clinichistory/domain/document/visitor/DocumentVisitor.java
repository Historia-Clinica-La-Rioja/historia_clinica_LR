package ar.lamansys.sgh.clinichistory.domain.document.visitor;

import ar.lamansys.sgh.clinichistory.domain.document.impl.AnestheticReportBo;
import ar.lamansys.sgh.clinichistory.domain.document.IDocumentBo;
import ar.lamansys.sgh.clinichistory.domain.document.impl.MedicationRequestBo;

public interface DocumentVisitor {
    default void visit(IDocumentBo documentBo) {}

    void visitAnestheticReport(AnestheticReportBo anestheticReport);

	void visitMedicationRequest(MedicationRequestBo medicationRequestBo);

}
