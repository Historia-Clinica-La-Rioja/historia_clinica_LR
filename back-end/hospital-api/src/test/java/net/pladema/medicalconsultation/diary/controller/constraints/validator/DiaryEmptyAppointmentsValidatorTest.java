package net.pladema.medicalconsultation.diary.controller.constraints.validator;

import net.pladema.medicalconsultation.appointment.service.AppointmentService;
import net.pladema.medicalconsultation.appointment.service.domain.AppointmentBo;
import net.pladema.medicalconsultation.diary.controller.dto.DiaryDto;
import net.pladema.medicalconsultation.diary.controller.dto.DiaryOpeningHoursDto;
import net.pladema.medicalconsultation.diary.controller.dto.OpeningHoursDto;
import net.pladema.sgx.dates.configuration.LocalDateMapper;
import net.pladema.sgx.validation.ValidationContextSetup;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
public class DiaryEmptyAppointmentsValidatorTest extends ValidationContextSetup {

	DiaryEmptyAppointmentsValidator diaryEmptyAppointmentsValidator;

	@MockBean
	private AppointmentService appointmentService;

	@MockBean
	private LocalDateMapper localDateMapper;


	@Before
	public void setUp() {
		super.setUp();
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
		AppointmentBo apb1 = new AppointmentBo(1, 1, 1, apbDate, apbHour, (short) 1, false, 1,"OSDE","20210220",null, (short) 1, null);
		List<AppointmentBo> returnFutureAppmets = Stream.of(apb1).collect(Collectors.toList());
		when(appointmentService.getFutureActiveAppointmentsByDiary(anyInt())).thenReturn(returnFutureAppmets);

	}

	private void setupContextInvalid() {
		LocalDate apbDate = LocalDate.parse("2020-08-12");
		LocalTime apbHour = LocalTime.parse("13:15:00");
		AppointmentBo apb1 = new AppointmentBo(1, 1, 1, apbDate, apbHour, (short) 1, false, 1, "OSDE", "20210220", null, (short) 2, null);
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
	public void test_AppointmentsValid() {
		setupContextValid();
		assertThat(diaryEmptyAppointmentsValidator.isValid(getDiaryDto(), contextMock))
			.isNotNull()
			.isTrue();

	}

	@Test
	public void test_AppointmentsInvalid() {
		setupContextInvalid();
		assertThat(diaryEmptyAppointmentsValidator.isValid(getDiaryDto(), contextMock))
			.isNotNull()
			.isFalse();
	}
}
