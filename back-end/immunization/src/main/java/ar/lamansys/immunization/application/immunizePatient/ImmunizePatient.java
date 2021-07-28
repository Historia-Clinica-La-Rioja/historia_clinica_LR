package ar.lamansys.immunization.application.immunizePatient;

import ar.lamansys.immunization.application.immunizePatient.exceptions.ImmunizePatientException;
import ar.lamansys.immunization.application.immunizePatient.exceptions.ImmunizePatientExceptionEnum;
import ar.lamansys.immunization.domain.appointment.ServeAppointmentStorage;
import ar.lamansys.immunization.domain.consultation.DoctorStorage;
import ar.lamansys.immunization.domain.consultation.ImmunizePatientBo;
import ar.lamansys.immunization.domain.consultation.VaccineConsultationBo;
import ar.lamansys.immunization.domain.consultation.VaccineConsultationStorage;
import ar.lamansys.immunization.domain.doctor.DoctorInfoBo;
import ar.lamansys.immunization.domain.immunization.ImmunizationDocumentBo;
import ar.lamansys.immunization.domain.immunization.ImmunizationDocumentStorage;
import ar.lamansys.immunization.domain.immunization.ImmunizationInfoBo;
import ar.lamansys.immunization.domain.immunization.ImmunizationValidator;
import ar.lamansys.immunization.domain.vaccine.VaccineRuleStorage;
import ar.lamansys.immunization.domain.vaccine.VaccineSchemeStorage;
import ar.lamansys.sgx.shared.dates.configuration.DateTimeProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

@Service
public class ImmunizePatient {

    private final Logger logger;

    private final VaccineConsultationStorage vaccineConsultationStorage;

    private final ImmunizationDocumentStorage immunizationDocumentStorage;

    private final DoctorStorage doctorStorage;

    private final VaccineSchemeStorage vaccineSchemeStorage;

    private final VaccineRuleStorage vaccineRuleStorage;

    private final ServeAppointmentStorage serveAppointmentStorage;

    private final DateTimeProvider dateTimeProvider;

    public ImmunizePatient(VaccineConsultationStorage vaccineConsultationStorage,
                           ImmunizationDocumentStorage immunizationDocumentStorage,
                           DoctorStorage doctorStorage,
                           VaccineSchemeStorage vaccineSchemeStorage,
                           VaccineRuleStorage vaccineRuleStorage,
                           ServeAppointmentStorage serveAppointmentStorage,
                           DateTimeProvider dateTimeProvider) {
        this.vaccineConsultationStorage = vaccineConsultationStorage;
        this.immunizationDocumentStorage = immunizationDocumentStorage;
        this.doctorStorage = doctorStorage;
        this.vaccineSchemeStorage = vaccineSchemeStorage;
        this.vaccineRuleStorage = vaccineRuleStorage;
        this.serveAppointmentStorage = serveAppointmentStorage;
        this.dateTimeProvider = dateTimeProvider;
        this.logger = LoggerFactory.getLogger(this.getClass());
    }

    @Transactional
    public void run(ImmunizePatientBo immunizePatientBo) {
        logger.debug("Input parameters -> immunize patient {}", immunizePatientBo);
        if (immunizePatientBo == null)
            throw new ImmunizePatientException(ImmunizePatientExceptionEnum.NULL_IMMUNIZATION_PATIENT, "La información de la inmunización es obligatoria");

        var doctorInfoBo = doctorStorage.getDoctorInfo().orElseThrow(() ->
                new ImmunizePatientException(ImmunizePatientExceptionEnum.INVALID_DOCTOR, "El identificador del profesional es invalido"));
        assertContextValid(immunizePatientBo, doctorInfoBo);

        LocalDate now = dateTimeProvider.nowDate();
        var encounterId = vaccineConsultationStorage.save(
                new VaccineConsultationBo(null,
                        immunizePatientBo.getPatientId(),
                        immunizePatientBo.getClinicalSpecialtyId(),
                        immunizePatientBo.getInstitutionId(),
                        doctorInfoBo.getId(),
                        now,
                        isBillable(immunizePatientBo)));
        immunizationDocumentStorage.save(mapTo(immunizePatientBo, encounterId, doctorInfoBo));

        serveAppointmentStorage.run(immunizePatientBo.getPatientId(), doctorInfoBo.getId(), now);
    }

    private boolean isBillable(ImmunizePatientBo immunizePatientBo) {
        return immunizePatientBo.getImmunizations().stream().anyMatch(ImmunizationInfoBo::isBillable);
    }

    private ImmunizationDocumentBo mapTo(ImmunizePatientBo immunizePatientBo, Integer encounterId, DoctorInfoBo doctorInfoBo) {
        return new ImmunizationDocumentBo(
                null,
                immunizePatientBo.getPatientId(),
                encounterId,
                immunizePatientBo.getInstitutionId(),
                doctorInfoBo.getId(),
                immunizePatientBo.getClinicalSpecialtyId(),
                immunizePatientBo.getImmunizations());
    }

    private void assertContextValid(ImmunizePatientBo immunizePatientBo, DoctorInfoBo doctorInfoBo) {
        if (immunizePatientBo.getInstitutionId() == null) //Validar que la institución es correcta.
            throw new ImmunizePatientException(ImmunizePatientExceptionEnum.NULL_INSTITUTION_ID, "El id de la institución es obligatorio");
        if (immunizePatientBo.getPatientId() == null)
            throw new ImmunizePatientException(ImmunizePatientExceptionEnum.NULL_PATIENT_ID, "El id del paciente es obligatorio");
        if (immunizePatientBo.getClinicalSpecialtyId() == null)
            throw new ImmunizePatientException(ImmunizePatientExceptionEnum.NULL_CLINICAL_SPECIALTY_ID, "El id de la especialidad clínica es obligatorio");
        if (!doctorInfoBo.hasSpecialty(immunizePatientBo.getClinicalSpecialtyId()))
            throw new ImmunizePatientException(ImmunizePatientExceptionEnum.INVALID_CLINICAL_SPECIALTY_ID, "La especialidad no pertenece al médico");
        var immunizationValidator = new ImmunizationValidator(vaccineSchemeStorage, vaccineRuleStorage);
        immunizePatientBo.getImmunizations().forEach(immunizationValidator::isValid);
    }
}
