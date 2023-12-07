package net.pladema.medicalconsultation.appointment.service.impl;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.pladema.medicalconsultation.appointment.controller.dto.AppointmentTicketDto;
import net.pladema.medicalconsultation.appointment.controller.dto.AppointmentTicketImageDto;
import net.pladema.medicalconsultation.appointment.service.TicketService;

import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@AllArgsConstructor
@Service
public class TicketServiceImpl implements TicketService {

	public static final String OUTPUT = "Output -> {}";

	@Override
	public String createOutputFileNameImage(AppointmentTicketImageDto dto) {
		log.debug("Input parameters -> AppointmentTicketImageDto {}", dto);
		String outputFileName = "Turno_" + dto.getPatientFullName() + "_" + dto.getDate() + "_" + dto.getHour();
		log.debug(OUTPUT, outputFileName);
		return outputFileName;
	}

	@Override
	public Map<String, Object> createContextImage(AppointmentTicketImageDto dto) {
		log.debug("Input parameters -> AppointmentTicketDto {}", dto);
		Map<String, Object> ctx = new HashMap<>();
		ctx.put("institution", dto.getInstitution());
		ctx.put("dni", dto.getDocumentNumber());
		ctx.put("patientFullName", dto.getPatientFullName());
		ctx.put("medicalCoverage", dto.getMedicalCoverage());
		ctx.put("date", dto.getDate());
		ctx.put("hour", dto.getHour());
		ctx.put("sectorName", dto.getSectorName());
		ctx.put("studyDescription", dto.getStudyDescription());
		log.debug(OUTPUT, ctx);
		return ctx;
	}

	@Override
	public String createOutputFileName(AppointmentTicketDto dto) {
		log.debug("Input parameters -> AppointmentTicketDto {}", dto);
		String outputFileName = "Turno_" + dto.getPatientFullName() + "_" + dto.getDate() + "_" + dto.getHour();
		log.debug(OUTPUT, outputFileName);
		return outputFileName;
	}

	@Override
	public Map<String, Object> createContext(AppointmentTicketDto dto) {
		log.debug("Input parameters -> AppointmentTicketDto {}", dto);
		Map<String, Object> ctx = new HashMap<>();
		ctx.put("institution", dto.getInstitution());
		ctx.put("dni", dto.getDocumentNumber());
		ctx.put("patientFullName", dto.getPatientFullName());
		ctx.put("medicalCoverage", dto.getMedicalCoverage());
		ctx.put("date", dto.getDate());
		ctx.put("hour", dto.getHour());
		ctx.put("doctorsOffice", dto.getDoctorsOffice());
		ctx.put("doctorFullName", dto.getDoctorFullName());
		log.debug(OUTPUT, ctx);
		return ctx;
	}
}
