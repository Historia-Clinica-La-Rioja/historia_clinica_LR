package net.pladema.medicalconsultation.appointment.service.impl;

import net.pladema.medicalconsultation.appointment.repository.AppointmentRepository;
import net.pladema.medicalconsultation.appointment.service.AppointmentService;
import net.pladema.medicalconsultation.appointment.service.domain.AppointmentBo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class AppointmentServiceImpl implements AppointmentService {

    public static final String OUTPUT = "Output -> {}";
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
            result = appointmentRepository.getAppointmentsByDiaries(diaryIds).stream().map(AppointmentBo::new).collect(Collectors.toList());
        LOG.debug(OUTPUT, result);
        return result;
    }

    @Override
    public boolean existAppointment(Integer diaryId, Integer openingHoursId, LocalDate date, LocalTime hour) {
        LOG.debug("Input parameters -> diaryId {}, openingHoursId {}, date {}, hour {}", diaryId, openingHoursId, date, hour);
        boolean result = appointmentRepository.existAppointment(diaryId, openingHoursId, date, hour);
        LOG.debug(OUTPUT, result);
        return result;
    }

    @Override
    public boolean updateState(Integer appointmentId, short appointmentStateId, Integer userId) {
        LOG.debug("Input parameters -> appointmentId {}, appointmentStateId {}, userId {}", appointmentId, appointmentStateId, userId);
        appointmentRepository.updateState(appointmentId, appointmentStateId, userId);
        LOG.debug(OUTPUT, Boolean.TRUE);
        return Boolean.TRUE;
    }


}
