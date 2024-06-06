package net.pladema.medicalconsultation.appointment.service.impl;

import ar.lamansys.sgx.shared.dates.configuration.LocalDateMapper;
import lombok.AllArgsConstructor;

import lombok.extern.slf4j.Slf4j;
import net.pladema.medicalconsultation.appointment.repository.GroupAppointmentRepository;

import net.pladema.medicalconsultation.appointment.repository.domain.BookingPersonBo;
import net.pladema.medicalconsultation.appointment.service.booking.BookingPersonService;
import net.pladema.medicalconsultation.appointment.service.domain.GroupAppointmentBo;

import net.pladema.medicalconsultation.appointment.service.domain.GroupAppointmentResponseBo;

import net.pladema.person.infraestructure.input.shared.SharedPersonImpl;

import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

@AllArgsConstructor
@Slf4j
@Service
public class GetAppointmentsFromDeterminatedDiaryDateTime {

	private final GroupAppointmentRepository groupAppointmentRepository;

	private final LocalDateMapper localDateMapper;

	private final SharedPersonImpl sharedPerson;

	private final BookingPersonService bookingPersonService;

	public List<GroupAppointmentResponseBo> run(GroupAppointmentBo groupAppointmentBo) {
		log.debug("Input parameters -> groupAppointmentBo {}", groupAppointmentBo);
		LocalDateTime localDateTime = localDateMapper.fromDateTimeDto(groupAppointmentBo.getDate());
		LocalDate date = localDateTime.toLocalDate();
		LocalTime hour = localDateTime.toLocalTime();
		List<GroupAppointmentResponseBo> result = groupAppointmentRepository.getApppointmentsFromDeterminatedDiaryDateTime(groupAppointmentBo.getDiaryId(), date, hour);
		result.forEach(groupAppointmentResponseBo -> {
			if (groupAppointmentResponseBo.getPersonId() != null) {
				String completeName = sharedPerson.getCompletePersonNameById(groupAppointmentResponseBo.getPersonId());
				groupAppointmentResponseBo.setPatientFullName(completeName);
			} else {
				setBookingCompleteData(groupAppointmentResponseBo);
			}
		});
		return result;
	}

	private void setBookingCompleteData(GroupAppointmentResponseBo groupAppointmentResponseBo) {
		Set<Integer> bookingAppointmentsIds = new HashSet<>(List.of(groupAppointmentResponseBo.getAppointmentId()));
		Map<Integer, BookingPersonBo> booking = bookingPersonService.getBookingPeople(bookingAppointmentsIds);
		String firstName = booking.get(groupAppointmentResponseBo.getAppointmentId()).getFirstName();
		String lastName = booking.get(groupAppointmentResponseBo.getAppointmentId()).getLastName();
		String identificationNumber = booking.get(groupAppointmentResponseBo.getAppointmentId()).getIdNumber();
		groupAppointmentResponseBo.setPatientFullName(firstName.concat(" ").concat(lastName));
		groupAppointmentResponseBo.setIdentificationNumber(identificationNumber);
	}
}
