package ar.lamansys.odontology.application.modifyOdontogramAndIndices;

import ar.lamansys.sgh.shared.infrastructure.input.service.odontology.OdontologyDiagnosticProcedureInfoDto;

import java.util.List;

public interface ModifyOdontogramAndIndices {

	void run(List<OdontologyDiagnosticProcedureInfoDto> odp, Integer newPatientId);

}
