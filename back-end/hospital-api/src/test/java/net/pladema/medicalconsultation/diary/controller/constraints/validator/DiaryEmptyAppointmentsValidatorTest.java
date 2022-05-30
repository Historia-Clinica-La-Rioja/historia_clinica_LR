package net.pladema.medicalconsultation.diary.controller.constraints.validator;

import ar.lamansys.sgx.shared.dates.configuration.LocalDateMapper;
import net.pladema.medicalconsultation.appointment.service.AppointmentService;
import net.pladema.medicalconsultation.appointment.service.domain.AppointmentBo;
import net.pladema.medicalconsultation.diary.controller.dto.DiaryDto;
import net.pladema.medicalconsultation.diary.controller.dto.DiaryOpeningHoursDto;
import net.pladema.medicalconsultation.diary.controller.dto.OpeningHoursDto;
import net.pladema.sgx.validation.ValidationContextSetup;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DiaryEmptyAppointmentsValidatorTest extends ValidationContextSetup {

	private DiaryEmptyAppointmentsValidator diaryEmptyAppointmentsValidator;

	@Mock
	private AppointmentService appointmentService;

	@Mock
	private LocalDateMapper localDateMapper;


	@BeforeEach
	void setUp() {
		diaryEmptyAppointmentsValidator = new DiaryEmptyAppointmentsValidator(appointmentService,
				localDateMapper);
		when(localDateMapper.fromStringToLocalDate("2020-08-01")).thenReturn(LocalDate.parse("2020-08-01"));
		when(localDateMapper.fromStringToLocalDate("2020-08-31")).thenReturn(LocalDate.parse("2020-08-31"));
		when(localDateMapper.fromStringToLocalTime("10:00:00")).thenReturn(LocalTime.parse("10:00:00"));
		when(localDateMapper.fromStringToLocalTime("12:00:00")).thenReturn(LocalTime.parse("12:00:00"));

	}

	private void setupContextValid() {
		LocalDate apbDate = LocalDate.parse("2020-08-12");
		LocalTime apbHour = LocalTime.parse("11:15:00");
		AppointmentBo apb1 = AppointmentBo.builder()
				.id(1).diaryId(1).patientId(1)
				.date(apbDate).hour(apbHour)
				.appointmentStateId((short) 1)
				.overturn(false)
				.openingHoursId(1)
				.medicalAttentionTypeId((short) 1)
				.phonePrefix("011")
				.phoneNumber("429784")
				.snomedId(55)
				.build();
		List<AppointmentBo> returnFutureAppmets = Stream.of(apb1).collect(Collectors.toList());
		when(appointmentService.getFutureActiveAppointmentsByDiary(anyInt())).thenReturn(returnFutureAppmets);

	}

	private void setupContextInvalid() {
		LocalDate apbDate = LocalDate.parse("2020-08-12");
		LocalTime apbHour = LocalTime.parse("13:15:00");
		AppointmentBo apb1 = AppointmentBo.builder()
				.id(1).diaryId(1).patientId(1)
				.date(apbDate).hour(apbHour)
				.appointmentStateId((short) 1)
				.overturn(false)
				.openingHoursId(1)
				.medicalAttentionTypeId((short) 2)
				.build();
		List<AppointmentBo> returnFutureAppmets = Stream.of(apb1).collect(Collectors.toList());
		when(appointmentService.getFutureActiveAppointmentsByDiary(anyInt())).thenReturn(returnFutureAppmets);

	}

	private DiaryDto getDiaryDto() {
		DiaryDto diary = new DiaryDto();
		diary.setAppointmentDuration((short) 20);
		diary.setAutomaticRenewal(false);
		DiaryOpeningHoursDto doh = new DiaryOpeningHoursDto();
		doh.setMedicalAttentionTypeId((short) 1);
		doh.setOverturnCount((short) 2);
		OpeningHoursDto oh = new OpeningHoursDto();
		oh.setDayWeekId((short) 3);
		oh.setFrom("10:00:00");
		oh.setTo("12:00:00");
		doh.setOpeningHours(oh);
		diary.setDiaryOpeningHours(Stream.of(doh).collect(Collectors.toList()));
		diary.setHealthcareProfessionalId(1);
		diary.setDoctorsOfficeId(1);
		diary.setStartDate("2020-08-01");
		diary.setEndDate("2020-08-31");
		diary.setAutomaticRenewal(true);
		diary.setId(1);
		return diary;
	}

	@Test
	void test_AppointmentsValid() {
		setupContextValid();
		Assertions.assertTrue(diaryEmptyAppointmentsValidator.isValid(getDiaryDto(), contextMock));

	}

	@Test
	void test_AppointmentsInvalid() {
		when(contextMock.buildConstraintViolationWithTemplate(any())).thenReturn(mockConstraintViolationBuilder);
		setupContextInvalid();
		when(contextMock.buildConstraintViolationWithTemplate(any())).thenReturn(mockConstraintViolationBuilder);
		Assertions.assertFalse(diaryEmptyAppointmentsValidator.isValid(getDiaryDto(), contextMock));
	}
}
