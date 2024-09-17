package ar.lamansys.sgh.shared.infrastructure.input.service.odontology;

import java.util.List;
import java.util.Optional;

public interface SharedOdontologyConsultationPort {

	List<Integer> getOdontologyConsultationIdsFromPatients(List<Integer> patients);
	List<OdontologyConsultationInfoDto> findAllById(List<Integer> ids);
	void deleteLastOdontogramDrawingFromPatient(Integer patientId);
	void deleteToothIndicesFromPatient(Integer patientId);
	void modifyLastOdontogramDrawing(List<OdontologyDiagnosticProcedureInfoDto> odp, Integer newPatientId);
	Optional<Integer> getPatientMedicalCoverageId(Integer id);
	void updateOdontogramDrawingFromHistoric(Integer patientId, Integer healthConditionId);
}
