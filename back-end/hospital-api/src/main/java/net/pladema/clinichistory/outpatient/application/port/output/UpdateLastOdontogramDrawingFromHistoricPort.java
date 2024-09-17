package net.pladema.clinichistory.outpatient.application.port.output;

public interface UpdateLastOdontogramDrawingFromHistoricPort {

	void run(Integer patientId, Integer healthConditionId);
}
