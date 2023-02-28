package net.pladema.medicalconsultation.appointment.service;

import net.pladema.medicalconsultation.appointment.repository.domain.AppointmentEquipmentShortSummaryBo;
import net.pladema.medicalconsultation.appointment.repository.domain.AppointmentShortSummaryBo;
import net.pladema.medicalconsultation.appointment.repository.domain.AppointmentTicketBo;
import net.pladema.medicalconsultation.appointment.service.domain.AppointmentAssignedBo;
import net.pladema.medicalconsultation.appointment.service.domain.AppointmentBo;
import net.pladema.medicalconsultation.appointment.service.domain.UpdateAppointmentBo;
import net.pladema.medicalconsultation.diary.service.domain.BlockBo;
import net.pladema.medicalconsultation.diary.service.domain.DiaryBo;
import net.pladema.patient.service.domain.PatientMedicalCoverageBo;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface EquipmentAppointmentService {

	Optional<AppointmentBo> getEquipmentAppointment(Integer appointmentId);

	boolean updateEquipmentState(Integer appointmentId, short appointmentStateId, Integer userId, String reason);

	AppointmentBo updateEquipmentAppointment(UpdateAppointmentBo appointmentDto);

    }
