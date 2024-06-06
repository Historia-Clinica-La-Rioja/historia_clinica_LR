package net.pladema.medicalconsultation.diary.controller;

import ar.lamansys.sgx.shared.dates.repository.entity.EDayOfWeek;

import com.fasterxml.jackson.databind.ObjectMapper;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.pladema.medicalconsultation.appointment.domain.enums.EAppointmentModality;
import net.pladema.medicalconsultation.appointment.service.domain.AppointmentSearchBo;
import net.pladema.medicalconsultation.diary.controller.dto.DiaryAvailableAppointmentsDto;
import net.pladema.medicalconsultation.diary.domain.DiaryAppointmentsSearchBo;
import net.pladema.medicalconsultation.diary.controller.mapper.DiaryMapper;
import net.pladema.medicalconsultation.diary.service.domain.DiaryAvailableAppointmentsBo;

import net.pladema.medicalconsultation.diary.service.DiaryAvailableAppointmentsService;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;



@RequestMapping("/institutions/{institutionId}/medicalConsultations/available-appointments")
@Tag(name = "Available Appointments Search", description = "Available Appointments Search")
@Validated
@RequiredArgsConstructor
@Slf4j
@RestController
public class DiaryAvailableAppointmentsSearchController {

	public static final String OUTPUT = "Output -> {}";

	private final DiaryMapper diaryMapper;
	private final DiaryAvailableAppointmentsService diaryAvailableAppointmentsService;
	private final ObjectMapper jackson;

	@GetMapping("/protected")
	@PreAuthorize("hasPermission(#institutionId, 'ADMINISTRATIVO, ESPECIALISTA_MEDICO, PROFESIONAL_DE_SALUD, ESPECIALISTA_EN_ODONTOLOGIA') ||" +
			" hasAnyAuthority('GESTOR_DE_ACCESO_DE_DOMINIO', 'GESTOR_DE_ACCESO_REGIONAL', 'GESTOR_DE_ACCESO_LOCAL')")
	public ResponseEntity<List<DiaryAvailableAppointmentsDto>> getAvailableProtectedAppointments(@PathVariable(name = "institutionId") Integer institutionId,
																										  @RequestParam String diaryProtectedAppointmentsSearch) {
		log.debug("Get all available protected appointments by filters {}, ", diaryProtectedAppointmentsSearch);
		DiaryAppointmentsSearchBo filter = parseFilter(diaryProtectedAppointmentsSearch);
		List<DiaryAvailableAppointmentsBo> diaryAvailableAppointmentsBoList = diaryAvailableAppointmentsService.getAvailableProtectedAppointmentsBySearchCriteria(filter, institutionId);
		List<DiaryAvailableAppointmentsDto> result = diaryAvailableAppointmentsBoList.stream().map(diaryMapper::toDiaryAvailableAppointmentsDto).collect(Collectors.toList());
		log.debug(OUTPUT, result);
		return ResponseEntity.ok(result);
	}

	@GetMapping("/third-party-booking")
	@PreAuthorize("hasAnyAuthority('GESTOR_CENTRO_LLAMADO')")
	public ResponseEntity<List<DiaryAvailableAppointmentsDto>> getAvailableAppointmentsToThirdPartyBooking(@PathVariable(name = "institutionId") Integer institutionId,
																										   @RequestParam String searchFilter) {
		log.debug("Get all available appointments for third party booking by filter {}, ", searchFilter);
		DiaryAppointmentsSearchBo filter = parseFilter(searchFilter);
		List<DiaryAvailableAppointmentsBo> availableAppointments = diaryAvailableAppointmentsService.getAvailableAppointmentsToThirdPartyBooking(filter, institutionId);
		List<DiaryAvailableAppointmentsDto> result = diaryMapper.toListDiaryAvailableAppointmentsDto(availableAppointments);
		log.debug(OUTPUT, result);
		return ResponseEntity.ok(result);
	}

	@GetMapping("/protected-quantity")
	@PreAuthorize("hasPermission(#institutionId, 'ENFERMERO, ESPECIALISTA_MEDICO, PROFESIONAL_DE_SALUD, ESPECIALISTA_EN_ODONTOLOGIA')")
	public ResponseEntity<Integer> getAvailableProtectedAppointmentsQuantity(@PathVariable(name = "institutionId") Integer institutionId,
																			 @RequestParam Integer careLineId,
																			 @RequestParam Integer departmentId,
																			 @RequestParam Integer institutionDestinationId,
																			 @RequestParam(value="clinicalSpecialtyIds", required = false) List<Integer> clinicalSpecialtyIds,
																			 @RequestParam(value="practiceSnomedId", required = false) Integer practiceSnomedId) {
		log.debug("Get available protected appointments quantity by careLineId {}, departmentId {}, institutionDestinationId {}, clinicalSpecialtyIds {}, practiceSnomedId {} ",
				careLineId, departmentId, institutionDestinationId, clinicalSpecialtyIds, practiceSnomedId);
		LocalDate from = LocalDate.now();
		LocalDate to = from.plusDays(60);
		Integer result = diaryAvailableAppointmentsService.getAvailableProtectedAppointmentsBySearchCriteria(new DiaryAppointmentsSearchBo(
				careLineId, clinicalSpecialtyIds, departmentId, institutionDestinationId, from, to, false, EAppointmentModality.NO_MODALITY, practiceSnomedId, true), institutionId).size();
		log.debug(OUTPUT, result);
		return ResponseEntity.ok(result);
	}

	@GetMapping("/quantity/by-reference-filter")
	@PreAuthorize("hasPermission(#institutionId, 'ENFERMERO, ESPECIALISTA_MEDICO, PROFESIONAL_DE_SALUD, ESPECIALISTA_EN_ODONTOLOGIA')")
	public ResponseEntity<Integer> getAvailableAppointmentsQuantity(@PathVariable(name = "institutionId") Integer institutionId,
																	@RequestParam Integer institutionDestinationId,
																	@RequestParam(value="clinicalSpecialtyIds", required = false) List<Integer> clinicalSpecialtyIds,
																	@RequestParam(value="practiceSnomedId", required = false) Integer practiceSnomedId) {
		log.debug("Get available appointments quantity for a reference by filters: institutionDestinationId {}, clinicalSpecialtyIds {}, practiceSnomedId {} ", institutionDestinationId,
				clinicalSpecialtyIds, practiceSnomedId);
		LocalDate from = LocalDate.now();
		LocalDate to = from.plusDays(60);
		AppointmentSearchBo appointmentSearch = new AppointmentSearchBo(EDayOfWeek.getAllIds(),null, LocalTime.MIN, LocalTime.MAX, from, to, practiceSnomedId);
		Integer result = diaryAvailableAppointmentsService.getAvailableAppointmentsBySearchCriteriaQuantity(institutionDestinationId, clinicalSpecialtyIds, appointmentSearch);
		log.debug(OUTPUT, result);
		return ResponseEntity.ok(result);
	}

	@GetMapping("/quantity/by-careline-diaries")
	@PreAuthorize("hasPermission(#institutionId, 'ENFERMERO, ESPECIALISTA_MEDICO, PROFESIONAL_DE_SALUD, ESPECIALISTA_EN_ODONTOLOGIA')")
	public ResponseEntity<Integer> getAvailableAppointmentsQuantityByCareLineDiaries(@PathVariable(name = "institutionId") Integer institutionId,
																					@RequestParam Integer institutionDestinationId,
																					@RequestParam Integer careLineId,
																					@RequestParam(value="practiceSnomedId", required = false) Integer practiceSnomedId,
																					@RequestParam(value="clinicalSpecialtyIds", required = false) List<Integer> clinicalSpecialtyIds) {
		log.debug("Get available appointments quantity in diaries based on careline and search criteria: institutionDestinationId {}, careLineId {}, " +
				"practiceSnomedId {}, clinicalSpecialtyIds {} ", institutionDestinationId, careLineId, practiceSnomedId, clinicalSpecialtyIds);
		LocalDate from = LocalDate.now();
		LocalDate to = from.plusDays(60);
		AppointmentSearchBo appointmentSearch = new AppointmentSearchBo(EDayOfWeek.getAllIds(),null, LocalTime.MIN, LocalTime.MAX, from, to, practiceSnomedId);
		Integer result = diaryAvailableAppointmentsService.getAvailableAppointmentsQuantityByCareLineDiaries(institutionDestinationId, clinicalSpecialtyIds, appointmentSearch, careLineId);
		log.debug(OUTPUT, result);
		return ResponseEntity.ok(result);
	}

	private DiaryAppointmentsSearchBo parseFilter(String diaryProtectedAppointmentsSearch) {
		DiaryAppointmentsSearchBo searchFilter = null;
		try {
			searchFilter = jackson.readValue(diaryProtectedAppointmentsSearch, DiaryAppointmentsSearchBo.class);
		} catch (IOException e) {
			log.error(String.format("Error mapping filter: %s", diaryProtectedAppointmentsSearch), e);
		}
		return searchFilter;
	}

}
