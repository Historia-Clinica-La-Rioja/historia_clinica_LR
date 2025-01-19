package ar.lamansys.sgh.clinichistory.application.document.visitors;

import ar.lamansys.sgh.clinichistory.application.anestheticreport.visitor.AnestheticReportVisitorMethods;
import ar.lamansys.sgh.clinichistory.application.fetchAllDocumentInfo.CompleteInformationForAllDocument;
import ar.lamansys.sgh.clinichistory.application.medicationrequest.FillOutMedicationRequest;
import ar.lamansys.sgh.clinichistory.domain.document.DocumentBo;
import ar.lamansys.sgh.clinichistory.domain.document.IDocumentBo;
import ar.lamansys.sgh.clinichistory.domain.document.impl.AnestheticReportBo;
import ar.lamansys.sgh.clinichistory.domain.document.impl.MedicationRequestBo;
import ar.lamansys.sgh.clinichistory.domain.document.visitor.DocumentVisitor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class FillOutDocumentVisitor implements DocumentVisitor {

    private final AnestheticReportVisitorMethods anestheticReportVisitorMethods;
    private final CompleteInformationForAllDocument completeInformationForAllDocument;
	private final FillOutMedicationRequest fillOutMedicationRequest;

    @Override
    public void visitAnestheticReport(AnestheticReportBo anestheticReport) {
        anestheticReportVisitorMethods.fillOutAnestheticReport(anestheticReport);
    }

	@Override
	public void visitMedicationRequest(MedicationRequestBo medicationRequestBo) {
		fillOutMedicationRequest.run(medicationRequestBo);
	}

	@Override
    public void visit(IDocumentBo documentBo) {
        completeInformationForAllDocument.run((DocumentBo) documentBo);
    }
}
