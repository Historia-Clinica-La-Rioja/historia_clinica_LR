package ar.lamansys.refcounterref.application.createcounterreference;

import ar.lamansys.refcounterref.application.createcounterreference.exceptions.CreateCounterReferenceException;
import ar.lamansys.refcounterref.application.createcounterreference.exceptions.CreateCounterReferenceExceptionEnum;
import ar.lamansys.refcounterref.application.port.CounterReferenceAppointmentStorage;
import ar.lamansys.refcounterref.application.port.CounterReferenceDoctorStorage;
import ar.lamansys.refcounterref.application.port.CounterReferenceDocumentStorage;
import ar.lamansys.refcounterref.application.port.CounterReferenceStorage;
import ar.lamansys.refcounterref.domain.counterreference.CounterReferenceBo;
import ar.lamansys.refcounterref.domain.counterreference.CounterReferenceClinicalTermsValidatorUtils;
import ar.lamansys.refcounterref.domain.document.CounterReferenceDocumentBo;
import ar.lamansys.refcounterref.domain.counterreference.CounterReferenceInfoBo;
import ar.lamansys.refcounterref.domain.doctor.CounterReferenceDoctorInfoBo;
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
    private final CounterReferenceAppointmentStorage counterReferenceAppointmentStorage;
    private final DateTimeProvider dateTimeProvider;
    private final CounterReferenceStorage counterReferenceStorage;
    private final CounterReferenceDocumentStorage counterReferenceDocumentStorage;

    @Transactional
    public void run(CounterReferenceBo counterReferenceBo) {

        log.debug("Input parameters -> counterReferenceBo {}", counterReferenceBo);

        var doctorInfoBo = counterReferenceDoctorStorage.getDoctorInfo().orElseThrow(() ->
                new CreateCounterReferenceException(CreateCounterReferenceExceptionEnum.INVALID_DOCTOR, "El identificador del profesional es invalido"));

        assertContextValid(counterReferenceBo, doctorInfoBo);

        LocalDate now = dateTimeProvider.nowDate();
        Integer medicalCoverageId = counterReferenceAppointmentStorage.getPatientMedicalCoverageId(counterReferenceBo.getPatientId(), doctorInfoBo.getId());

        var encounterId = counterReferenceStorage.save(
                new CounterReferenceInfoBo(null,
                        counterReferenceBo.getReferenceId(),
                        counterReferenceBo.getPatientId(),
                        medicalCoverageId,
                        counterReferenceBo.getInstitutionId(),
                        counterReferenceBo.getClinicalSpecialtyId(),
                        now,
                        doctorInfoBo.getId(),
                        true,
                        counterReferenceBo.getFileIds(),
						counterReferenceBo.getClosureTypeId()));

        counterReferenceDocumentStorage.save(new CounterReferenceDocumentBo(null, counterReferenceBo, encounterId, doctorInfoBo.getId(), now));

        counterReferenceAppointmentStorage.run(counterReferenceBo.getPatientId(), doctorInfoBo.getId(), now);
    }

    private void assertContextValid(CounterReferenceBo counterReferenceBo, CounterReferenceDoctorInfoBo doctorInfoBo) {
        if (counterReferenceBo.getInstitutionId() == null)
            throw new CreateCounterReferenceException(CreateCounterReferenceExceptionEnum.NULL_INSTITUTION_ID, "El id de la institución es obligatorio");
        if (counterReferenceBo.getCounterReferenceNote() == null)
            throw new CreateCounterReferenceException(CreateCounterReferenceExceptionEnum.NULL_COUNTER_REFERENCE_NOTE, "La contrarreferencia es un dato obligatorio");
        if (counterReferenceBo.getPatientId() == null)
            throw new CreateCounterReferenceException(CreateCounterReferenceExceptionEnum.NULL_PATIENT_ID, "El id del paciente es obligatorio");
        if (counterReferenceBo.getClinicalSpecialtyId() == null)
            throw new CreateCounterReferenceException(CreateCounterReferenceExceptionEnum.NULL_CLINICAL_SPECIALTY_ID, "El id de especialidad es obligatorio");
        if (!doctorInfoBo.hasSpecialty(counterReferenceBo.getClinicalSpecialtyId()))
            throw new CreateCounterReferenceException(CreateCounterReferenceExceptionEnum.INVALID_CLINICAL_SPECIALTY_ID, "El doctor no posee la especialidad indicada");
        assertThereAreNoRepeatedConcepts(counterReferenceBo);
    }

    private void assertThereAreNoRepeatedConcepts(CounterReferenceBo counterReferenceBo) {
        CreateCounterReferenceException exception = new CreateCounterReferenceException(CreateCounterReferenceExceptionEnum.REPEATED_CONCEPTS);
        if (CounterReferenceClinicalTermsValidatorUtils.repeatedClinicalTerms(counterReferenceBo.getProcedures()))
            exception.addError("Procedimientos repetidos");
        if (CounterReferenceClinicalTermsValidatorUtils.repeatedClinicalTerms(counterReferenceBo.getMedications()))
            exception.addError("Medicaciones repetidas");
        if (CounterReferenceClinicalTermsValidatorUtils.repeatedClinicalTerms(counterReferenceBo.getAllergies()))
            exception.addError("Alergias repetidas");

        if (exception.hasErrors())
            throw exception;
    }
    
}
