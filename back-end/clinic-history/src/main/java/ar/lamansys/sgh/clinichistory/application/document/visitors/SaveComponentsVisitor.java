package ar.lamansys.sgh.clinichistory.application.document.visitors;

import ar.lamansys.sgh.clinichistory.application.anestheticreport.visitor.AnestheticReportVisitorMethods;
import ar.lamansys.sgh.clinichistory.application.medicationrequest.SaveMedicationRequestComponents;
import ar.lamansys.sgh.clinichistory.domain.document.impl.MedicationRequestBo;
import ar.lamansys.sgh.clinichistory.domain.document.visitor.DocumentVisitor;
import ar.lamansys.sgh.clinichistory.domain.document.impl.AnestheticReportBo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class SaveComponentsVisitor implements DocumentVisitor {

    private final AnestheticReportVisitorMethods anestheticReportVisitorMethods;

	private final SaveMedicationRequestComponents saveMedicationRequestComponents;
    
    @Override
    public void visitAnestheticReport(AnestheticReportBo documentBo) {
        anestheticReportVisitorMethods.saveComponents(documentBo);
    }

	@Override
	public void visitMedicationRequest(MedicationRequestBo medicationRequestBo) {
		saveMedicationRequestComponents.run(medicationRequestBo);
	}

}
