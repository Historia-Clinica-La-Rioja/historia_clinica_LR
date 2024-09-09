package ar.lamansys.odontology.application.modifyOdontogramAndIndices;

import ar.lamansys.odontology.application.createConsultation.DrawOdontogramService;
import ar.lamansys.odontology.application.createConsultation.exceptions.CreateConsultationException;
import ar.lamansys.odontology.application.createConsultation.exceptions.CreateConsultationExceptionEnum;
import ar.lamansys.odontology.application.odontogram.GetToothService;
import ar.lamansys.odontology.application.odontogram.GetToothSurfacesService;
import ar.lamansys.odontology.domain.DiagnosticBo;
import ar.lamansys.odontology.domain.DiagnosticStorage;
import ar.lamansys.odontology.domain.OdontologySnomedBo;
import ar.lamansys.odontology.domain.ProcedureBo;
import ar.lamansys.odontology.domain.ProcedureStorage;
import ar.lamansys.odontology.domain.consultation.ConsultationCpoCeoIndicesStorage;
import ar.lamansys.odontology.domain.consultation.ConsultationDentalActionBo;
import ar.lamansys.odontology.domain.consultation.OdontologyConsultationStorage;
import ar.lamansys.odontology.domain.consultation.ToothIndicesStorage;
import ar.lamansys.sgh.shared.infrastructure.input.service.SharedSnomedPort;
import ar.lamansys.sgh.shared.infrastructure.input.service.odontology.OdontologyDiagnosticProcedureInfoDto;
import lombok.AllArgsConstructor;

import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
@Slf4j
public class ModifyOdontogramAndIndicesImpl implements ModifyOdontogramAndIndices{

	private final SharedSnomedPort sharedSnomedPort;
	private final GetToothService getToothService;
	private final GetToothSurfacesService getToothSurfacesService;
	private final ToothIndicesStorage toothIndicesStorage;
	private final DrawOdontogramService drawOdontogramService;
	private final DiagnosticStorage diagnosticStorage;
	private final ProcedureStorage procedureStorage;

	private final ConsultationCpoCeoIndicesStorage consultationCpoCeoIndicesStorage;

	private final OdontologyConsultationStorage odontologyConsultationStorage;

	@Override
	public void run(List<OdontologyDiagnosticProcedureInfoDto> odp, Integer newPatientId) {
		log.debug("Input parameters -> DiagnosticProceduresInfo{}, patientId{}",odp,newPatientId);
		List<ConsultationDentalActionBo> cda = new ArrayList<>();
		odp.forEach(o-> {

			var snomedAction = sharedSnomedPort.getSnomed(o.getSnomedId());
			var snomedTooth = sharedSnomedPort.getSnomed(o.getToothId());

			OdontologySnomedBo osAction = new OdontologySnomedBo(snomedAction.getSctid(), snomedAction.getPt());
			OdontologySnomedBo osTooth = new OdontologySnomedBo(snomedTooth.getSctid(), snomedTooth.getPt());

			var tooth = getToothService.run(snomedTooth.getSctid());

			ConsultationDentalActionBo cdaToAdd = new ConsultationDentalActionBo(osAction,osTooth,null, tooth.isTemporary(),o.isDiagnostic());

			if (o.isDiagnostic()) {
				Optional<DiagnosticBo> opDiagnostic = diagnosticStorage.getDiagnostic(snomedAction.getSctid());
				if (opDiagnostic.isEmpty())
					throw new CreateConsultationException(CreateConsultationExceptionEnum.DENTAL_DIAGNOSTIC_NOT_FOUND,
							"El diagnóstico con ID de Snomed: '" + snomedAction.getSctid() +
									"' y término: '" + snomedAction.getPt() + "' no es un diagnóstico dental aplicable");
				DiagnosticBo diagnostic = opDiagnostic.get();
				cdaToAdd.setPermanentIndex(diagnostic.getPermanentIndex());
				cdaToAdd.setTemporaryIndex(diagnostic.getTemporaryIndex());
			} else {
				Optional<ProcedureBo> opProcedure = procedureStorage.getProcedure(snomedAction.getSctid());
				if (opProcedure.isEmpty())
					throw new CreateConsultationException(CreateConsultationExceptionEnum.DENTAL_PROCEDURE_NOT_FOUND,
							"El procedimiento con ID de Snomed: '" + snomedAction.getSctid() +
									"' y término: '" + snomedAction.getPt() + "' no es un procedimiento dental aplicable");
				ProcedureBo procedure = opProcedure.get();
				cdaToAdd.setPermanentIndex(procedure.getPermanentIndex());
				cdaToAdd.setTemporaryIndex(procedure.getTemporaryIndex());
			}

			if (o.getSurfaceId() != null) {
				var snomedSurface = sharedSnomedPort.getSnomed(o.getSurfaceId());
				OdontologySnomedBo osSurface = new OdontologySnomedBo(snomedSurface.getSctid(), snomedSurface.getPt());
				cdaToAdd.setSurface(osSurface);
				var surface = getToothSurfacesService.run(snomedTooth.getSctid());
				cdaToAdd.setSurfacePosition(surface.getSurfacePosition(osSurface));
			}

			cda.add(cdaToAdd);
		});

		var indices = toothIndicesStorage.computeIndices(newPatientId,cda);

		var lastOdontologyConsultation = odontologyConsultationStorage.getLastByPatientId(newPatientId);
		var result = drawOdontogramService.run(newPatientId,cda, lastOdontologyConsultation.getId());
		indices.setConsultationDate(lastOdontologyConsultation.getUpdatedOn());
		consultationCpoCeoIndicesStorage.saveIndices(lastOdontologyConsultation.getId(),indices);

		log.debug("Output -> ToothDrawings{}, CpoCeiIndices{}", result, indices);
	}
}
