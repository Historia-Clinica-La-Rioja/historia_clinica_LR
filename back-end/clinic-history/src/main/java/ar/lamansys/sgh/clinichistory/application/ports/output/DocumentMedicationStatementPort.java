package ar.lamansys.sgh.clinichistory.application.ports.output;

import ar.lamansys.sgh.clinichistory.domain.GetCommercialStatementCommercialPrescriptionBo;

import java.util.List;

public interface DocumentMedicationStatementPort {

	List<GetCommercialStatementCommercialPrescriptionBo> getCommercialStatementCommercialPrescriptionByDocumentId(Long documentId);

}
