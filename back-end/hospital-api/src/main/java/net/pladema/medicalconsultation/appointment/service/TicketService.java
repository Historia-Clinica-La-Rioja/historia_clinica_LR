package net.pladema.medicalconsultation.appointment.service;

import net.pladema.medicalconsultation.appointment.controller.dto.AppointmentTicketDto;
import net.pladema.medicalconsultation.appointment.controller.dto.AppointmentTicketImageDto;

import java.util.Map;

public interface TicketService {

	String createOutputFileNameImage(AppointmentTicketImageDto dto);

	Map<String, Object> createContextImage(AppointmentTicketImageDto dto);

	String createOutputFileName(AppointmentTicketDto dto);

	Map<String, Object> createContext(AppointmentTicketDto dto);
}
