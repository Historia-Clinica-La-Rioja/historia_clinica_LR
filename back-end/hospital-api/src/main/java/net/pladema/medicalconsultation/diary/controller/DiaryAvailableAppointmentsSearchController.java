package net.pladema.medicalconsultation.diary.controller;

import ar.lamansys.sgx.shared.dates.repository.entity.EDayOfWeek;

import com.fasterxml.jackson.databind.ObjectMapper;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.pladema.medicalconsultation.appointment.domain.enums.EAppointmentModality;
import net.pladema.medicalconsultation.appointment.service.domain.AppointmentSearchBo;
import net.pladema.medicalconsultation.diary.controller.dto.DiaryProtectedAppointmentsSearch;
import net.pladema.medicalconsultation.diary.controller.mapper.DiaryMapper;
import net.pladema.medicalconsultation.diary.service.domain.DiaryAvailableProtectedAppointmentsBo;
import net.pladema.medicalconsultation.diary.controller.dto.DiaryAvailableProtectedAppointmentsDto;

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
	@PreAuthorize("hasPermission(#institutionId, 'ADMINISTRATIVO, ESPECIALISTA_MEDICO, PROFESIONAL_DE_SALUD, ESPECIALISTA_EN_ODONTOLOGIA')")
	public ResponseEntity<List<DiaryAvailableProtectedAppointmentsDto>> getAvailableProtectedAppointments(@PathVariable(name = "institutionId") Integer institutionId,
																										  @RequestParam String diaryProtectedAppointmentsSearch) {
		log.debug("Get all available protected appointments by filters {}, ", diaryProtectedAppointmentsSearch);
		List<DiaryAvailableProtectedAppointmentsBo> diaryAvailableProtectedAppointmentsBoList = diaryAvailableAppointmentsService.getAvailableProtectedAppointmentsBySearchCriteria(parseFilter(diaryProtectedAppointmentsSearch), institutionId);
		List<DiaryAvailableProtectedAppointmentsDto> result = diaryAvailableProtectedAppointmentsBoList.stream().map(diaryMapper::toDiaryAvailableProtectedAppointmentsDto).collect(Collectors.toList());
		log.debug(OUTPUT, result);
		return ResponseEntity.ok(result);
	}

	@GetMapping("/protected-quantity")
	@PreAuthorize("hasPermission(#institutionId, 'ENFERMERO, ESPECIALISTA_MEDICO, PROFESIONAL_DE_SALUD, ESPECIALISTA_EN_ODONTOLOGIA')")
	public ResponseEntity<Integer> getAvailableProtectedAppointmentsQuantity(@PathVariable(name = "institutionId") Integer institutionId,
																			 @RequestParam Integer careLineId,
																			 @RequestParam Integer departmentId,
																			 @RequestParam Integer institutionDestinationId,
																			 @RequestParam(value="clinicalSpecialtyId", required = false) Integer clinicalSpecialtyId,
																			 @RequestParam(value="practiceSnomedId", required = false) Integer practiceSnomedId) {
		log.debug("Get available protected appointments quantity by careLineId {}, departmentId {}, institutionDestinationId {}, clinicalSpecialtyId {}, practiceSnomedId {} ",
				careLineId, departmentId, institutionDestinationId, clinicalSpecialtyId, practiceSnomedId);
		LocalDate from = LocalDate.now();
		LocalDate to = from.plusDays(60);
		Integer result = diaryAvailableAppointmentsService.getAvailableProtectedAppointmentsBySearchCriteria(new DiaryProtectedAppointmentsSearch(
				careLineId, clinicalSpecialtyId, departmentId, institutionDestinationId, from, to, false, EAppointmentModality.NO_MODALITY, practiceSnomedId), institutionId).size();
		log.debug(OUTPUT, result);
		return ResponseEntity.ok(result);
	}

	@GetMapping("/quantity/by-reference-filter")
	@PreAuthorize("hasPermission(#institutionId, 'ENFERMERO, ESPECIALISTA_MEDICO, PROFESIONAL_DE_SALUD, ESPECIALISTA_EN_ODONTOLOGIA')")
	public ResponseEntity<Integer> getAvailableAppointmentsQuantity(@PathVariable(name = "institutionId") Integer institutionId,
																	@RequestParam Integer institutionDestinationId,
																	@RequestParam(value="clinicalSpecialtyId", required = false) Integer clinicalSpecialtyId,
																	@RequestParam(value="practiceSnomedId", required = false) Integer practiceSnomedId) {
		log.debug("Get available appointments quantity for a reference by filters: institutionDestinationId {}, clinicalSpecialtyId {}, practiceSnomedId {} ", institutionDestinationId,
				clinicalSpecialtyId, practiceSnomedId);
		LocalDate from = LocalDate.now();
		LocalDate to = from.plusDays(60);
		AppointmentSearchBo appointmentSearch = new AppointmentSearchBo(EDayOfWeek.getAllIds(),null, LocalTime.MIN, LocalTime.MAX, from, to, practiceSnomedId);
		Integer result = diaryAvailableAppointmentsService.geAvailableAppointmentsBySearchCriteriaQuantity(institutionDestinationId, clinicalSpecialtyId, appointmentSearch);
		log.debug(OUTPUT, result);
		return ResponseEntity.ok(result);
	}

	@GetMapping("/quantity/by-careline-diaries")
	@PreAuthorize("hasPermission(#institutionId, 'ENFERMERO, ESPECIALISTA_MEDICO, PROFESIONAL_DE_SALUD, ESPECIALISTA_EN_ODONTOLOGIA')")
	public ResponseEntity<Integer> geAvailableAppointmentsQuantityByCareLineDiaries(@PathVariable(name = "institutionId") Integer institutionId,
																					@RequestParam Integer institutionDestinationId,
																					@RequestParam Integer careLineId,
																					@RequestParam(value="practiceSnomedId", required = false) Integer practiceSnomedId,
																					@RequestParam(value="clinicalSpecialtyId", required = false) Integer clinicalSpecialtyId) {
		log.debug("Get available appointments quantity in diaries based on careline and search criteria: institutionDestinationId {}, careLineId {}, " +
				"practiceSnomedId {}, clinicalSpecialtyId {} ", institutionDestinationId, careLineId, practiceSnomedId, clinicalSpecialtyId);
		LocalDate from = LocalDate.now();
		LocalDate to = from.plusDays(60);
		AppointmentSearchBo appointmentSearch = new AppointmentSearchBo(EDayOfWeek.getAllIds(),null, LocalTime.MIN, LocalTime.MAX, from, to, practiceSnomedId);
		Integer result = diaryAvailableAppointmentsService.geAvailableAppointmentsQuantityByCareLineDiaries(institutionDestinationId, clinicalSpecialtyId, appointmentSearch, careLineId);
		log.debug(OUTPUT, result);
		return ResponseEntity.ok(result);
	}

	private DiaryProtectedAppointmentsSearch parseFilter(String diaryProtectedAppointmentsSearch) {
		DiaryProtectedAppointmentsSearch searchFilter = null;
		try {
			searchFilter = jackson.readValue(diaryProtectedAppointmentsSearch, DiaryProtectedAppointmentsSearch.class);
		} catch (IOException e) {
			log.error(String.format("Error mapping filter: %s", diaryProtectedAppointmentsSearch), e);
		}
		return searchFilter;
	}

}
