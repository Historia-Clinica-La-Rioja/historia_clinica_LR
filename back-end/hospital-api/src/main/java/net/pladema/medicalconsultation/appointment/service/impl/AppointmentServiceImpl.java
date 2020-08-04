package net.pladema.medicalconsultation.appointment.service.impl;

import net.pladema.medicalconsultation.appointment.repository.AppointmentRepository;
import net.pladema.medicalconsultation.appointment.service.AppointmentService;
import net.pladema.medicalconsultation.appointment.service.domain.AppointmentBo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Service
public class AppointmentServiceImpl implements AppointmentService {

    private static final Logger LOG = LoggerFactory.getLogger(AppointmentServiceImpl.class);

    private final AppointmentRepository appointmentRepository;

    public AppointmentServiceImpl(AppointmentRepository appointmentRepository){
        super();
        this.appointmentRepository = appointmentRepository;
    }

    @Override
    public Collection<AppointmentBo> getAppointmentsByDiaries(List<Integer> diaryIds) {
        LOG.debug("Input parameters -> diaryIds {}", diaryIds);
        Collection<AppointmentBo> result = new ArrayList<>();
        if (!diaryIds.isEmpty())
            appointmentRepository.getAppointmentsByDiaries(diaryIds).stream().map(AppointmentBo::new);
        LOG.debug("Output -> {}", result);
        return result;
    }
}
