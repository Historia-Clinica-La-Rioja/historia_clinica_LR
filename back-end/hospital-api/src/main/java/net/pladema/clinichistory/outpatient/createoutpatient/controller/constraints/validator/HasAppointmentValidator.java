package net.pladema.clinichistory.outpatient.createoutpatient.controller.constraints.validator;

import net.pladema.clinichistory.outpatient.createoutpatient.controller.constraints.HasAppointment;
import net.pladema.medicalconsultation.appointment.controller.service.AppointmentExternalService;
import ar.lamansys.sgx.shared.dates.configuration.DateTimeProvider;
import ar.lamansys.sgx.shared.security.UserInfo;
import net.pladema.staff.controller.service.HealthcareProfessionalExternalService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class HasAppointmentValidator implements ConstraintValidator<HasAppointment, Integer> {

    private static final Logger LOG = LoggerFactory.getLogger(HasAppointmentValidator.class);

    private final AppointmentExternalService appointmentExternalService;

    private final HealthcareProfessionalExternalService healthcareProfessionalExternalService;

    private final DateTimeProvider dateTimeProvider;

    @Value("${test.stress.disable.validation:false}")
    private boolean disableValidation;

    @Value("${habilitar.boton.consulta:false}")
    private boolean enableNewConsultation;


    public HasAppointmentValidator(AppointmentExternalService appointmentExternalService,
                                   HealthcareProfessionalExternalService healthcareProfessionalExternalService,
                                   DateTimeProvider dateTimeProvider) {
        this.appointmentExternalService = appointmentExternalService;
        this.healthcareProfessionalExternalService = healthcareProfessionalExternalService;
        this.dateTimeProvider = dateTimeProvider;
    }

    @Override
    public void initialize(HasAppointment constraintAnnotation) {
    	//empty until done
    }

    @Override
    public boolean isValid(Integer patientId, ConstraintValidatorContext context) {
       LOG.debug("Input parameters -> patientId {}", patientId);
       Integer healthcareProfessionalId = healthcareProfessionalExternalService.getProfessionalId(UserInfo.getCurrentAuditor());
       boolean result = disableValidation || enableNewConsultation || appointmentExternalService.hasCurrentAppointment(patientId, healthcareProfessionalId, dateTimeProvider.nowDate());
       LOG.debug("OUTPUT -> {}", result);
       return result;
    }
}
