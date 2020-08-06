package net.pladema.medicalconsultation.appointment.service.impl;

import net.pladema.medicalconsultation.appointment.repository.AppointmentAssnRepository;
import net.pladema.medicalconsultation.appointment.repository.AppointmentRepository;
import net.pladema.medicalconsultation.appointment.repository.entity.Appointment;
import net.pladema.medicalconsultation.appointment.repository.entity.AppointmentAssn;
import net.pladema.medicalconsultation.appointment.service.CreateAppointmentService;
import net.pladema.medicalconsultation.appointment.service.domain.AppointmentBo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class CreateAppointmentServiceImpl implements CreateAppointmentService {

    private static final Logger LOG = LoggerFactory.getLogger(CreateAppointmentServiceImpl.class);

    private final AppointmentRepository appointmentRepository;

    private final AppointmentAssnRepository appointmentAssnRepository;

    public CreateAppointmentServiceImpl(AppointmentRepository appointmentRepository,
                                        AppointmentAssnRepository appointmentAssnRepository){
        super();
        this.appointmentRepository = appointmentRepository;
        this.appointmentAssnRepository = appointmentAssnRepository;
    }

    @Override
    public AppointmentBo execute(AppointmentBo appointmentBo) {
        LOG.debug("Input parameters -> appointmentBo {}", appointmentBo);
        Appointment appointment = Appointment.newFromAppointmentBo(appointmentBo);
        appointment = appointmentRepository.save(appointment);
        AppointmentBo result = AppointmentBo.newFromAppointment(appointment);

        appointmentAssnRepository.save(new AppointmentAssn(appointmentBo.getDiaryId(),
                appointmentBo.getOpeningHoursId(), appointment.getId()));

        LOG.debug("Output -> {}", result);
        return result;
    }
}
