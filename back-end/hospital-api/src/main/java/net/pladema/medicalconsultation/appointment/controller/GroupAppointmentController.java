package net.pladema.medicalconsultation.appointment.controller;

import com.fasterxml.jackson.core.JsonProcessingException;

import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import net.pladema.medicalconsultation.appointment.controller.dto.GroupAppointmentResponseDto;
import net.pladema.medicalconsultation.appointment.controller.mapper.AppointmentMapper;
import net.pladema.medicalconsultation.appointment.service.domain.GroupAppointmentBo;
import net.pladema.medicalconsultation.appointment.service.domain.GroupAppointmentResponseBo;
import net.pladema.medicalconsultation.appointment.service.impl.GetAppointmentsFromDeterminatedDiaryDateTime;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/institutions/{institutionId}/medicalConsultations/appointments/group-appointments/")
@RestController
public class GroupAppointmentController {

	private final GetAppointmentsFromDeterminatedDiaryDateTime getAppointmentsFromDeterminatedDiaryDateTime;

	private final AppointmentMapper appointmentMapper;

	private final ObjectMapper objectMapper;

	@GetMapping("get-appointments-from-determinated-diary-date-time")
	@PreAuthorize("hasPermission(#institutionId, 'ADMINISTRATIVO, ESPECIALISTA_MEDICO, PROFESIONAL_DE_SALUD, ESPECIALISTA_EN_ODONTOLOGIA, ENFERMERO, ADMINISTRADOR_AGENDA')")
	public ResponseEntity<List<GroupAppointmentResponseDto>> getAppointmentsFromDeterminatedDiaryDateTime(@PathVariable("institutionId") Integer institutionId,
																										  @RequestParam("data") String data) throws JsonProcessingException {
		log.debug("Input parameters -> institutionId {}, data {}", institutionId, data);
		GroupAppointmentBo groupAppointmentBo = initializeGroupAppointmentBo(data);
		List<GroupAppointmentResponseBo> boList = getAppointmentsFromDeterminatedDiaryDateTime.run(groupAppointmentBo);
		List<GroupAppointmentResponseDto> dtoList = boList.stream().map(appointmentMapper::toGroupAppointmentDto).collect(Collectors.toList());
		return ResponseEntity.ok(dtoList);
	}

	private GroupAppointmentBo initializeGroupAppointmentBo(String data) throws JsonProcessingException {
		GroupAppointmentBo groupAppointmentBo = objectMapper.readValue(data, GroupAppointmentBo.class);
		if (groupAppointmentBo == null)
			groupAppointmentBo = new GroupAppointmentBo();
		return groupAppointmentBo;
	}
}
