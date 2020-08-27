package net.pladema.clinichistory.outpatient.createoutpatient.controller.constraints.validator;

import net.pladema.clinichistory.outpatient.createoutpatient.controller.constraints.HasAppointment;
import net.pladema.medicalconsultation.appointment.controller.service.AppointmentExternalService;
import net.pladema.sgx.security.utils.UserInfo;
import net.pladema.staff.controller.service.HealthcareProfessionalExternalService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class HasAppointmentValidator implements ConstraintValidator<HasAppointment, Integer> {

    private static final Logger LOG = LoggerFactory.getLogger(HasAppointmentValidator.class);

    private final AppointmentExternalService appointmentExternalService;

    private final HealthcareProfessionalExternalService healthcareProfessionalExternalService;

    public HasAppointmentValidator(AppointmentExternalService appointmentExternalService,
                                   HealthcareProfessionalExternalService healthcareProfessionalExternalService) {
        this.appointmentExternalService = appointmentExternalService;
        this.healthcareProfessionalExternalService = healthcareProfessionalExternalService;
    }

    @Override
    public void initialize(HasAppointment constraintAnnotation) {
    	//empty until done
    }

    @Override
    public boolean isValid(Integer patientId, ConstraintValidatorContext context) {
       LOG.debug("Input parameters -> patientId {}", patientId);
       Integer healthcareProfessionalId = healthcareProfessionalExternalService.getProfessionalId(UserInfo.getCurrentAuditor());
       boolean result = appointmentExternalService.hasConfirmedAppointment(patientId, healthcareProfessionalId);
       LOG.debug("OUTPUT -> {}", result);
       return result;
    }
}
