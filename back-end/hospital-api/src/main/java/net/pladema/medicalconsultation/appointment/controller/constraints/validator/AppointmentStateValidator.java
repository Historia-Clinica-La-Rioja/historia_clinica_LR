package net.pladema.medicalconsultation.appointment.controller.constraints.validator;

import net.pladema.medicalconsultation.appointment.controller.constraints.ValidAppointmentState;
import net.pladema.medicalconsultation.appointment.service.AppointmentMasterDataService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class AppointmentStateValidator implements ConstraintValidator<ValidAppointmentState, String> {

    private static final Logger LOG = LoggerFactory.getLogger(AppointmentStateValidator.class);

    private final AppointmentMasterDataService appointmentMasterDataService;

    public AppointmentStateValidator(AppointmentMasterDataService appointmentMasterDataService) {
        this.appointmentMasterDataService = appointmentMasterDataService;
    }


    @Override
    public void initialize(ValidAppointmentState constraintAnnotation) {
        // nothing to do
    }

    @Override
    public boolean isValid(String appointmentStateId, ConstraintValidatorContext context) {
        LOG.debug("Input parameters -> appointmentStateId {}", appointmentStateId);
        return appointmentMasterDataService.validAppointmentStateId(Short.parseShort(appointmentStateId));
    }


}
