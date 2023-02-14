package ar.lamansys.sgh.shared.infrastructure.input.service.odontology;

import java.util.List;

public interface SharedOdontologyConsultationPort {

	List<Integer> getOdontologyConsultationIdsFromPatients(List<Integer> patients);
	List<OdontologyConsultationInfoDto> findAllById(List<Integer> ids);
	void deleteLastOdontogramDrawingFromPatient(Integer patientId);
	void deleteToothIndicesFromPatient(Integer patientId);
	void modifyLastOdontogramDrawing(List<OdontologyDiagnosticProcedureInfoDto> odp, Integer newPatientId);
}
