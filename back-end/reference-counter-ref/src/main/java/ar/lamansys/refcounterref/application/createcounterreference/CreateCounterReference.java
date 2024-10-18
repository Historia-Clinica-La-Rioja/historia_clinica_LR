package ar.lamansys.refcounterref.application.createcounterreference;

import ar.lamansys.refcounterref.application.createcounterreference.exceptions.CreateCounterReferenceException;
import ar.lamansys.refcounterref.application.createcounterreference.exceptions.CreateCounterReferenceExceptionEnum;
import ar.lamansys.refcounterref.application.port.ReferenceCounterReferenceAppointmentStorage;
import ar.lamansys.refcounterref.application.port.CounterReferenceDoctorStorage;
import ar.lamansys.refcounterref.application.port.CounterReferenceDocumentStorage;
import ar.lamansys.refcounterref.application.port.CounterReferenceStorage;
import ar.lamansys.refcounterref.application.port.ReferenceStorage;
import ar.lamansys.refcounterref.domain.counterreference.CounterReferenceBo;
import ar.lamansys.refcounterref.domain.counterreference.CounterReferenceClinicalTermsValidatorUtils;
import ar.lamansys.refcounterref.domain.document.CounterReferenceDocumentBo;
import ar.lamansys.refcounterref.domain.counterreference.CounterReferenceInfoBo;
import ar.lamansys.refcounterref.domain.doctor.CounterReferenceDoctorInfoBo;
import ar.lamansys.refcounterref.domain.enums.EReferenceClosureType;
import ar.lamansys.refcounterref.domain.enums.EReferenceRegulationState;
import ar.lamansys.refcounterref.domain.enums.EReferenceStatus;
import ar.lamansys.refcounterref.infraestructure.output.repository.reference.Reference;
import ar.lamansys.sgh.shared.infrastructure.input.service.SharedNotePort;
import ar.lamansys.sgx.shared.dates.configuration.DateTimeProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

@Slf4j
@RequiredArgsConstructor
@Service
public class CreateCounterReference {

    private final CounterReferenceDoctorStorage counterReferenceDoctorStorage;
    private final ReferenceCounterReferenceAppointmentStorage referenceCounterReferenceAppointmentStorage;
    private final DateTimeProvider dateTimeProvider;
    private final CounterReferenceStorage counterReferenceStorage;
    private final CounterReferenceDocumentStorage counterReferenceDocumentStorage;
	private final ReferenceStorage referenceStorage;
	private final SharedNotePort sharedNotePort;

    @Transactional
    public void run(CounterReferenceBo counterReferenceBo) {

        log.debug("Input parameters -> counterReferenceBo {}", counterReferenceBo);

		CounterReferenceDoctorInfoBo doctorInfoBo = validate(counterReferenceBo);

		LocalDate now = dateTimeProvider.nowDate();

		var noteId = sharedNotePort.saveNote(counterReferenceBo.getCounterReferenceNote());

        var encounterId = counterReferenceStorage.save(
                new CounterReferenceInfoBo(null,
                        counterReferenceBo.getReferenceId(),
                        counterReferenceBo.getPatientId(),
                        counterReferenceBo.getPatientMedicalCoverageId(),
                        counterReferenceBo.getInstitutionId(),
                        counterReferenceBo.getClinicalSpecialtyId(),
                        now,
                        doctorInfoBo.getId(),
                        true,
                        counterReferenceBo.getFileIds(),
						counterReferenceBo.getClosureTypeId(),
						counterReferenceBo.getHierarchicalUnitId(),
						noteId));

		counterReferenceDocumentStorage.save(new CounterReferenceDocumentBo(null, counterReferenceBo, encounterId, doctorInfoBo.getId(), now));

		referenceCounterReferenceAppointmentStorage.run(counterReferenceBo.getPatientId(), doctorInfoBo.getId(), now);
    }

	public void runValidations(CounterReferenceBo counterReferenceBo) {
		log.debug("Input parameters -> counterReferenceBo {}", counterReferenceBo);
		validate(counterReferenceBo);
	}

	private CounterReferenceDoctorInfoBo validate(CounterReferenceBo counterReferenceBo) {
		boolean referenceIsClosed = counterReferenceStorage.existsCounterReference(counterReferenceBo.getReferenceId());

		assertValidCounterReference(referenceIsClosed);

		var referenceData = referenceStorage.findById(counterReferenceBo.getReferenceId()).orElseThrow(() -> new CreateCounterReferenceException(CreateCounterReferenceExceptionEnum.INVALID_REFERENCE, "El identificador de la referencia es invalido"));

		var doctorInfoBo = counterReferenceDoctorStorage.getDoctorInfo().orElseThrow(() -> new CreateCounterReferenceException(CreateCounterReferenceExceptionEnum.INVALID_DOCTOR, "El identificador del profesional es invalido"));

		assertContextValid(counterReferenceBo, doctorInfoBo, referenceData.getConsultation());

		assertValidReferenceStatus(referenceData);

		counterReferenceBo.setPatientMedicalCoverageId(referenceCounterReferenceAppointmentStorage.getPatientMedicalCoverageId(counterReferenceBo.getPatientId(), doctorInfoBo.getId()));
		return doctorInfoBo;
	}

    private void assertContextValid(CounterReferenceBo counterReferenceBo, CounterReferenceDoctorInfoBo doctorInfoBo, boolean consultation) {
		if (counterReferenceBo.getClosureTypeId().equals(EReferenceClosureType.CIERRE_ADMINISTRATIVO.getId()))
			throw new CreateCounterReferenceException(CreateCounterReferenceExceptionEnum.INVALID_CLOSURE_TYPE, "No cuenta con suficientes privilegios para darle un cierre administrativo a la referencia");
		if (counterReferenceBo.getInstitutionId() == null)
            throw new CreateCounterReferenceException(CreateCounterReferenceExceptionEnum.NULL_INSTITUTION_ID, "El id de la institución es obligatorio");
        if (counterReferenceBo.getCounterReferenceNote() == null)
            throw new CreateCounterReferenceException(CreateCounterReferenceExceptionEnum.NULL_COUNTER_REFERENCE_NOTE, "La contrarreferencia es un dato obligatorio");
        if (counterReferenceBo.getPatientId() == null)
            throw new CreateCounterReferenceException(CreateCounterReferenceExceptionEnum.NULL_PATIENT_ID, "El id del paciente es obligatorio");
		if (consultation && counterReferenceBo.getClinicalSpecialtyId() == null)
			throw new CreateCounterReferenceException(CreateCounterReferenceExceptionEnum.NULL_CLINICAL_SPECIALTY_ID, "El id de especialidad es obligatorio");
        if (counterReferenceBo.getClinicalSpecialtyId() != null && !doctorInfoBo.hasSpecialty(counterReferenceBo.getClinicalSpecialtyId()))
            throw new CreateCounterReferenceException(CreateCounterReferenceExceptionEnum.INVALID_CLINICAL_SPECIALTY_ID, "El doctor no posee la especialidad indicada");
        assertThereAreNoRepeatedConcepts(counterReferenceBo);

    }

    private void assertThereAreNoRepeatedConcepts(CounterReferenceBo counterReferenceBo) {
        CreateCounterReferenceException exception = new CreateCounterReferenceException(CreateCounterReferenceExceptionEnum.REPEATED_CONCEPTS);
        if (CounterReferenceClinicalTermsValidatorUtils.repeatedClinicalTerms(counterReferenceBo.getProcedures()))
            exception.addError("Procedimientos repetidos");
        if (CounterReferenceClinicalTermsValidatorUtils.repeatedClinicalTerms(counterReferenceBo.getMedications()))
            exception.addError("Medicaciones repetidas");
        if (CounterReferenceClinicalTermsValidatorUtils.repeatedClinicalTerms(counterReferenceBo.getAllergies().getContent()))
            exception.addError("Alergias repetidas");

        if (exception.hasErrors())
            throw exception;
    }

	private void assertValidReferenceStatus(Reference reference){
		if (!reference.getStatusId().equals(EReferenceStatus.ACTIVE.getId()))
			throw new CreateCounterReferenceException(CreateCounterReferenceExceptionEnum.INVALID_REFERENCE_STATUS, "La referencia debe estar activa");
		if (!reference.getRegulationStateId().equals(EReferenceRegulationState.APPROVED.getId()))
			throw new CreateCounterReferenceException(CreateCounterReferenceExceptionEnum.INVALID_REFERENCE_REGULATION_STATE, "La referencia debe haber sido aprobada previamente");
	}

	private void assertValidCounterReference(boolean referenceIsClosed){
		if (referenceIsClosed){
			throw new CreateCounterReferenceException(CreateCounterReferenceExceptionEnum.CLOSED_REFERENCE, "La referencia ya posee un cierre");
		}
	}
    
}
