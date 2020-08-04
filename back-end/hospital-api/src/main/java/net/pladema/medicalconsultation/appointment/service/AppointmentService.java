package net.pladema.medicalconsultation.appointment.service;

import net.pladema.medicalconsultation.appointment.service.domain.AppointmentBo;

import java.util.Collection;
import java.util.List;

public interface AppointmentService {

    Collection<AppointmentBo> getAppointmentsByDiaries(List<Integer> diaryIds);
}
