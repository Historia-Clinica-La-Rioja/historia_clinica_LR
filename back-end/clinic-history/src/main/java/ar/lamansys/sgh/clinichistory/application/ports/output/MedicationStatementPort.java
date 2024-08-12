package ar.lamansys.sgh.clinichistory.application.ports.output;

public interface MedicationStatementPort {

	Short fetchMedicationStatementLineStateById(Integer medicationStatementId);

	Double getMedicationStatementQuantityById(Integer medicationStatementId);
}
