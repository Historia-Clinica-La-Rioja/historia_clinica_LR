package ar.lamansys.odontology.infrastructure.input.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import ar.lamansys.odontology.application.modifyOdontogramAndIndices.ModifyOdontogramAndIndicesImpl;
import ar.lamansys.odontology.application.odontogram.UpdateLastOdontogramDrawingFromHistoricImpl;

import org.springframework.stereotype.Service;

import ar.lamansys.odontology.application.odontogram.ports.OdontogramDrawingStorage;
import ar.lamansys.odontology.application.odontogram.ports.OdontologyConsultationStorage;
import ar.lamansys.odontology.application.odontogram.ports.ToothIndicesStorage;
import ar.lamansys.sgh.shared.infrastructure.input.service.odontology.OdontologyConsultationInfoDto;
import ar.lamansys.sgh.shared.infrastructure.input.service.odontology.OdontologyDiagnosticProcedureInfoDto;
import ar.lamansys.sgh.shared.infrastructure.input.service.odontology.SharedOdontologyConsultationPort;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class SharedOdontologyConsultationPortImpl implements SharedOdontologyConsultationPort {

	private final OdontologyConsultationStorage odontologyConsultationStorage;
	private final OdontogramDrawingStorage odontogramDrawingStorage;
	private final ToothIndicesStorage toothIndicesStorage;
	private final ModifyOdontogramAndIndicesImpl modifyOdontogramAndIndices;
	private final UpdateLastOdontogramDrawingFromHistoricImpl updateLastOdontogramDrawingFromHistoric;

	@Override
	public List<Integer> getOdontologyConsultationIdsFromPatients(List<Integer> patients) {
		return odontologyConsultationStorage.getOdontologyConsultationIdsFromPatients(patients);
	}

	@Override
	public List<OdontologyConsultationInfoDto> findAllById(List<Integer> ids) {
		return odontologyConsultationStorage.findAllById(ids).stream().map(oc -> new OdontologyConsultationInfoDto(
				oc.getId(),
				oc.getPatientId(),
				oc.getClinicalSpecialtyId(),
				oc.getInstitutionId(),
				oc.getPatientMedicalCoverageId(),
				oc.getDoctorId(),
				oc.getPerformedDate(),
				oc.getBillable())).collect(Collectors.toList());
	}

	@Override
	public void deleteLastOdontogramDrawingFromPatient(Integer patientId) {
		odontogramDrawingStorage.deleteByPatientId(patientId);
	}

	@Override
	public void deleteToothIndicesFromPatient(Integer patientId) {
		toothIndicesStorage.deleteByPatientId(patientId);
	}

	@Override
	public void modifyLastOdontogramDrawing(List<OdontologyDiagnosticProcedureInfoDto> odp, Integer newPatientId) {
		modifyOdontogramAndIndices.run(odp,newPatientId);
	}

	@Override
	public Optional<Integer> getPatientMedicalCoverageId(Integer id){
		return odontologyConsultationStorage.getPatientMedicalCoverageId(id);
	}

	@Override
	public void updateOdontogramDrawingFromHistoric(Integer patientId, Integer healthConditionId) {
		updateLastOdontogramDrawingFromHistoric.run(patientId, healthConditionId);
	}

	@Override
	public Optional<Long> getOdontologyDocumentId(Integer healthConditionId) {
		return odontologyConsultationStorage.getOdontologyDocumentId(healthConditionId);
	}

}
