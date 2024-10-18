package ar.lamansys.refcounterref.application.administrativeclosurereference;

import ar.lamansys.refcounterref.application.createcounterreference.exceptions.CreateCounterReferenceException;
import ar.lamansys.refcounterref.application.createcounterreference.exceptions.CreateCounterReferenceExceptionEnum;
import ar.lamansys.refcounterref.application.port.CounterReferenceStorage;
import ar.lamansys.refcounterref.application.port.ReferenceAppointmentStorage;
import ar.lamansys.refcounterref.application.port.ReferenceStorage;
import ar.lamansys.refcounterref.domain.counterreference.ReferenceAdministrativeClosureBo;
import ar.lamansys.refcounterref.domain.counterreference.CounterReferenceInfoBo;
import ar.lamansys.refcounterref.domain.enums.EReferenceClosureType;
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

	@Transactional
	public void run(ReferenceAdministrativeClosureBo closure) {
		log.debug("Input parameters -> closure {}", closure);
		assertValid(closure);
		var patientId = referenceStorage.getPatientId(closure.getReferenceId());
		var noteId = sharedNotePort.saveNote(closure.getClosureNote());
	 	closure.setPatientId(patientId);
		counterReferenceStorage.save(mapToCounterReferenceInfoBo(closure, noteId));
	}

	private CounterReferenceInfoBo mapToCounterReferenceInfoBo(ReferenceAdministrativeClosureBo closure, Long noteId) {
		var result = new CounterReferenceInfoBo(closure);
		result.setClosureTypeId(EReferenceClosureType.CIERRE_ADMINISTRATIVO.getId());
		result.setNoteId(noteId);
		return result;
	}

	private void assertValid(ReferenceAdministrativeClosureBo closure) {
		var referenceId = closure.getReferenceId();
		boolean referenceIsClosed = counterReferenceStorage.existsCounterReference(referenceId);
		if (referenceIsClosed)
			throw new CreateCounterReferenceException(CreateCounterReferenceExceptionEnum.CLOSED_REFERENCE, "La referencia ya posee un cierre");
		boolean referenceHasAppointment = referenceAppointmentStorage.referenceHasAppointment(referenceId);
		if (referenceHasAppointment)
			throw new CreateCounterReferenceException(CreateCounterReferenceExceptionEnum.REFERENCE_HAS_APPOINTMENT, "La referencia posee un turno asignado");
		if (closure.getClosureNote() == null)
			throw new CreateCounterReferenceException(CreateCounterReferenceExceptionEnum.NULL_COUNTER_REFERENCE_NOTE, "La observación es un dato obligatorio");
	}

}
