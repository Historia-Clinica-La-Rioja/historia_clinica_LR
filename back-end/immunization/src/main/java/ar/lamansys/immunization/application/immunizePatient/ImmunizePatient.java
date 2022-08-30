package ar.lamansys.immunization.application.immunizePatient;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ar.lamansys.immunization.application.immunizePatient.exceptions.ImmunizePatientException;
import ar.lamansys.immunization.application.immunizePatient.exceptions.ImmunizePatientExceptionEnum;
import ar.lamansys.immunization.domain.appointment.AppointmentStorage;
import ar.lamansys.immunization.domain.consultation.DoctorStorage;
import ar.lamansys.immunization.domain.consultation.ImmunizePatientBo;
import ar.lamansys.immunization.domain.consultation.VaccineConsultationBo;
import ar.lamansys.immunization.domain.consultation.VaccineConsultationStorage;
import ar.lamansys.immunization.domain.doctor.DoctorInfoBo;
import ar.lamansys.immunization.domain.immunization.ImmunizationDocumentBo;
import ar.lamansys.immunization.domain.immunization.ImmunizationDocumentStorage;
import ar.lamansys.immunization.domain.immunization.ImmunizationInfoBo;
import ar.lamansys.immunization.domain.immunization.ImmunizationValidator;
import ar.lamansys.immunization.domain.user.ImmunizationUserStorage;
import ar.lamansys.immunization.domain.user.RolePermissionException;
import ar.lamansys.immunization.domain.user.RolesExceptionEnum;
import ar.lamansys.immunization.domain.vaccine.VaccineConditionApplicationStorage;
import ar.lamansys.immunization.domain.vaccine.VaccineRuleStorage;
import ar.lamansys.immunization.domain.vaccine.VaccineSchemeStorage;
import ar.lamansys.sgh.shared.infrastructure.input.service.appointment.dto.DocumentAppointmentDto;
import ar.lamansys.sgx.shared.dates.configuration.DateTimeProvider;

@Service
public class ImmunizePatient {

    private final Logger logger;

    private final AppointmentStorage appointmentStorage;

    private final DoctorStorage doctorStorage;

    private final DateTimeProvider dateTimeProvider;

    private final ImmunizationDocumentStorage immunizationDocumentStorage;

    private final ImmunizationUserStorage immunizationUserStorage;

    private final VaccineConsultationStorage vaccineConsultationStorage;

    private final VaccineConditionApplicationStorage vaccineConditionApplicationStorage;

    private final VaccineSchemeStorage vaccineSchemeStorage;

    private final VaccineRuleStorage vaccineRuleStorage;
    public ImmunizePatient(AppointmentStorage appointmentStorage,
                           DateTimeProvider dateTimeProvider,
                           DoctorStorage doctorStorage,
                           ImmunizationDocumentStorage immunizationDocumentStorage,
                           ImmunizationUserStorage immunizationUserStorage,
                           VaccineConditionApplicationStorage vaccineConditionApplicationStorage,
                           VaccineConsultationStorage vaccineConsultationStorage,
                           VaccineRuleStorage vaccineRuleStorage,
                           VaccineSchemeStorage vaccineSchemeStorage
                           ) {
        this.vaccineConsultationStorage = vaccineConsultationStorage;
        this.immunizationDocumentStorage = immunizationDocumentStorage;
        this.doctorStorage = doctorStorage;
        this.immunizationUserStorage = immunizationUserStorage;
        this.vaccineConditionApplicationStorage = vaccineConditionApplicationStorage;
        this.vaccineSchemeStorage = vaccineSchemeStorage;
        this.vaccineRuleStorage = vaccineRuleStorage;
        this.appointmentStorage = appointmentStorage;
        this.dateTimeProvider = dateTimeProvider;
        this.logger = LoggerFactory.getLogger(this.getClass());
    }

    @Transactional
    public DocumentAppointmentDto run(ImmunizePatientBo immunizePatientBo) {
        logger.debug("Input parameters -> immunize patient {}", immunizePatientBo);
        if (immunizePatientBo == null)
            throw new ImmunizePatientException(ImmunizePatientExceptionEnum.NULL_IMMUNIZATION_PATIENT, "La información de la inmunización es obligatoria");

        var doctorInfoBo = doctorStorage.getDoctorInfo().orElseThrow(() ->
                new ImmunizePatientException(ImmunizePatientExceptionEnum.INVALID_DOCTOR, "El identificador del profesional es invalido"));
        assertContextValid(immunizePatientBo, doctorInfoBo);

        LocalDate now = dateTimeProvider.nowDate();
        Integer medicalCoverageId = appointmentStorage.getPatientMedicalCoverageId(immunizePatientBo.getPatientId(), doctorInfoBo.getId());
        var encounterId = vaccineConsultationStorage.save(
                new VaccineConsultationBo(null,
                        immunizePatientBo.getPatientId(),
                        medicalCoverageId,
                        immunizePatientBo.getClinicalSpecialtyId(),
                        immunizePatientBo.getInstitutionId(),
                        doctorInfoBo.getId(),
                        now,
                        isBillable(immunizePatientBo)));
        var documentId = immunizationDocumentStorage.save(mapTo(immunizePatientBo, encounterId, doctorInfoBo, now));
		Integer appointmentId = null;

        if (gettingVaccine(immunizePatientBo.getImmunizations())) {
			appointmentId = appointmentStorage.run(immunizePatientBo.getPatientId(), doctorInfoBo.getId(), now);
		}

		if(appointmentId != null)
			return new DocumentAppointmentDto(documentId, appointmentId);
		return null;
    }

    private boolean gettingVaccine(List<ImmunizationInfoBo> immunizations) {
        return immunizations.stream().anyMatch(ImmunizationInfoBo::isBillable);
    }

    private boolean isBillable(ImmunizePatientBo immunizePatientBo) {
        return immunizePatientBo.getImmunizations().stream().anyMatch(ImmunizationInfoBo::isBillable);
    }

    private ImmunizationDocumentBo mapTo(ImmunizePatientBo immunizePatientBo, Integer encounterId,
                                         DoctorInfoBo doctorInfoBo, LocalDate now) {
        return new ImmunizationDocumentBo(
                null,
                immunizePatientBo.getPatientId(),
                encounterId,
                immunizePatientBo.getInstitutionId(),
                doctorInfoBo.getId(),
                immunizePatientBo.getClinicalSpecialtyId(),
                immunizePatientBo.getImmunizations(),
                now);
    }

    private void assertContextValid(ImmunizePatientBo immunizePatientBo, DoctorInfoBo doctorInfoBo) {
        if (immunizePatientBo.getInstitutionId() == null) //Validar que la institución es correcta.
            throw new ImmunizePatientException(ImmunizePatientExceptionEnum.NULL_INSTITUTION_ID, "El id de la institución es obligatorio");
        validatePermission(immunizePatientBo.getInstitutionId(), immunizePatientBo.getImmunizations());
        if (immunizePatientBo.getPatientId() == null)
            throw new ImmunizePatientException(ImmunizePatientExceptionEnum.NULL_PATIENT_ID, "El id del paciente es obligatorio");
        if (immunizePatientBo.getClinicalSpecialtyId() == null)
            throw new ImmunizePatientException(ImmunizePatientExceptionEnum.NULL_CLINICAL_SPECIALTY_ID, "El id de la especialidad clínica es obligatorio");
        if (!doctorInfoBo.hasSpecialty(immunizePatientBo.getClinicalSpecialtyId()))
            throw new ImmunizePatientException(ImmunizePatientExceptionEnum.INVALID_CLINICAL_SPECIALTY_ID, "La especialidad no pertenece al médico");
        var immunizationValidator = new ImmunizationValidator(vaccineConditionApplicationStorage, vaccineSchemeStorage, vaccineRuleStorage);
        immunizePatientBo.getImmunizations().forEach(immunizationValidator::isValid);
    }

    private void validatePermission(Integer institutionId, List<ImmunizationInfoBo> immunizations) {
        var rolesOverInstitution = immunizationUserStorage.fetchLoggedUserRoles().stream()
                .filter(roleInfoBo -> institutionId.equals(roleInfoBo.getInstitution()))
                .collect(Collectors.toList());
        if (rolesOverInstitution.stream().noneMatch(roleInfoBo -> roleInfoBo.anyRole(List.of("ENFERMERO", "PROFESIONAL_DE_SALUD", "ESPECIALISTA_EN_ODONTOLOGIA", "ESPECIALISTA_MEDICO"))))
            throw new RolePermissionException(RolesExceptionEnum.INVALID_ROLE, "No tiene los permisos suficientes para inmunizar un paciente");
        if (rolesOverInstitution.stream().anyMatch(roleInfoBo -> roleInfoBo.anyRole(List.of("ENFERMERO"))))
            return;
        if (rolesOverInstitution.stream().anyMatch(roleInfoBo -> roleInfoBo.anyRole(List.of("PROFESIONAL_DE_SALUD", "ESPECIALISTA_EN_ODONTOLOGIA", "ESPECIALISTA_MEDICO")))
            && immunizations.stream().anyMatch(ImmunizationInfoBo::isBillable))
            throw new RolePermissionException(RolesExceptionEnum.INVALID_ROLE, "El enfermero solo tiene permisos para aplicar vacunas");
    }

}
