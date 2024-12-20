package ar.lamansys.sgh.clinichistory.application.ports.output;

import ar.lamansys.sgh.clinichistory.domain.CommercialPrescriptionDataBo;

import java.util.List;

public interface MedicationStatementPort {

	Short fetchMedicationStatementLineStateById(Integer medicationStatementId);

	Double getMedicationStatementQuantityById(Integer medicationStatementId);

	List<CommercialPrescriptionDataBo> getCommercialPrescriptionDataByIds(List<Integer> medicationStatementIds);

}
