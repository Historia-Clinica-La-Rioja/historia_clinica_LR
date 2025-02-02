package ar.lamansys.refcounterref.application.administrativeclosurereference;

import ar.lamansys.refcounterref.application.createcounterreference.exceptions.CreateCounterReferenceException;
import ar.lamansys.refcounterref.application.createcounterreference.exceptions.CreateCounterReferenceExceptionEnum;
import ar.lamansys.refcounterref.application.port.CounterReferenceStorage;
import ar.lamansys.refcounterref.application.port.ReferenceAppointmentStorage;
import ar.lamansys.refcounterref.application.port.ReferenceStorage;
import ar.lamansys.refcounterref.domain.counterreference.ReferenceAdministrativeClosureBo;
import ar.lamansys.refcounterref.domain.counterreference.CounterReferenceInfoBo;
import ar.lamansys.refcounterref.domain.enums.EReferenceClosureType;
import ar.lamansys.sgh.shared.infrastructure.input.service.SharedDiagnosticReportPort;
import ar.lamansys.sgh.shared.infrastructure.input.service.SharedLoggedUserPort;
import ar.lamansys.sgh.shared.infrastructure.input.service.SharedNotePort;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@RequiredArgsConstructor
@Service
public class AdministrativeReferenceClosure {

	private final CounterReferenceStorage counterReferenceStorage;

	private final ReferenceStorage referenceStorage;

	private final ReferenceAppointmentStorage referenceAppointmentStorage;

	private final SharedNotePort sharedNotePort;
	private final SharedDiagnosticReportPort sharedDiagnosticReportPort;

	private final SharedLoggedUserPort sharedLoggedUserPort;

	@Transactional
	public void run(ReferenceAdministrativeClosureBo closure, Integer institutionId) {
		log.debug("Input parameters -> closure {}, institutionId {}", closure, institutionId);
		assertValid(closure, institutionId);
		var referenceId = closure.getReferenceId();
		var patientId = referenceStorage.getPatientId(referenceId);
        referenceStorage.getServiceRequestId(referenceId).ifPresent(srId -> completeStudy(srId, patientId, closure.getClosureNote()));
		var noteId = sharedNotePort.saveNote(closure.getClosureNote());
	 	closure.setPatientId(patientId);
		counterReferenceStorage.save(mapToCounterReferenceInfoBo(closure, noteId));
	}

	private void completeStudy(Integer serviceRequestId, Integer patientId, String notes) {
		sharedDiagnosticReportPort.completeDiagnosticReport(serviceRequestId, patientId, notes);
	}

	private CounterReferenceInfoBo mapToCounterReferenceInfoBo(ReferenceAdministrativeClosureBo closure, Long noteId) {
		var result = new CounterReferenceInfoBo(closure);
		result.setClosureTypeId(EReferenceClosureType.CIERRE_ADMINISTRATIVO.getId());
		result.setNoteId(noteId);
		result.setFileIds(closure.getFileIds());
		return result;
	}

	private void assertValid(ReferenceAdministrativeClosureBo closure, Integer institutionId) {
		var referenceId = closure.getReferenceId();
		validateInstitutionalManagerAction(referenceId, institutionId);
		boolean referenceIsClosed = counterReferenceStorage.existsCounterReference(referenceId);
		if (referenceIsClosed)
			throw new CreateCounterReferenceException(CreateCounterReferenceExceptionEnum.CLOSED_REFERENCE, "La referencia ya posee un cierre");
		boolean referenceHasAppointment = referenceAppointmentStorage.referenceHasAppointment(referenceId);
		if (referenceHasAppointment)
			throw new CreateCounterReferenceException(CreateCounterReferenceExceptionEnum.REFERENCE_HAS_APPOINTMENT, "La referencia posee un turno asignado");
		if (closure.getClosureNote() == null)
			throw new CreateCounterReferenceException(CreateCounterReferenceExceptionEnum.NULL_COUNTER_REFERENCE_NOTE, "La observación es un dato obligatorio");
	}

	private void validateInstitutionalManagerAction(Integer referenceId, Integer institutionId) {
		var userHasInstitutionalManagerRole = sharedLoggedUserPort.hasInstitutionalManagerRole();
		if (!userHasInstitutionalManagerRole)
			return;
		var originInstitutionId = referenceStorage.getOriginInstitutionId(referenceId);
		if (!originInstitutionId.equals(institutionId))
			throw new CreateCounterReferenceException(CreateCounterReferenceExceptionEnum.INVALID_REFERENCE, "No es posible cerrar una solicitud realizada en una institucion ajena a su usuario");
	}
}
