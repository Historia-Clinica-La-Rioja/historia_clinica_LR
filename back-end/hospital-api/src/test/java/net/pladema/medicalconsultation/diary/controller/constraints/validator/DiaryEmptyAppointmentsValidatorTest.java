package net.pladema.medicalconsultation.diary.controller.constraints.validator;

import ar.lamansys.sgx.shared.dates.configuration.LocalDateMapper;
import ar.lamansys.sgx.shared.featureflags.application.FeatureFlagsService;
import net.pladema.medicalconsultation.appointment.domain.UpdateDiaryAppointmentBo;
import net.pladema.medicalconsultation.diary.application.port.output.DiaryPort;
import net.pladema.medicalconsultation.diary.controller.dto.DiaryDto;
import net.pladema.medicalconsultation.diary.controller.dto.DiaryOpeningHoursDto;
import net.pladema.medicalconsultation.diary.controller.dto.OpeningHoursDto;
import net.pladema.medicalconsultation.diary.service.DiaryOpeningHoursService;
import net.pladema.medicalconsultation.diary.service.DiaryService;
import net.pladema.medicalconsultation.diary.service.domain.DiaryBo;
import net.pladema.medicalconsultation.diary.service.domain.DiaryOpeningHoursBo;
import net.pladema.medicalconsultation.diary.service.domain.OpeningHoursBo;
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
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DiaryEmptyAppointmentsValidatorTest extends ValidationContextSetup {

	private DiaryEmptyAppointmentsValidator diaryEmptyAppointmentsValidator;

	@Mock
	private LocalDateMapper localDateMapper;

	@Mock
	private FeatureFlagsService featureFlagsService;

	@Mock
	private DiaryService diaryService;

	@Mock
	private DiaryOpeningHoursService diaryOpeningHoursService;

	@Mock
	private DiaryPort diaryPort;


	@BeforeEach
	void setUp() {
		diaryEmptyAppointmentsValidator = new DiaryEmptyAppointmentsValidator(localDateMapper, featureFlagsService, diaryService, diaryOpeningHoursService, diaryPort);
		when(localDateMapper.fromStringToLocalDate("2020-08-01")).thenReturn(LocalDate.parse("2020-08-01"));
		when(localDateMapper.fromStringToLocalDate("2020-08-31")).thenReturn(LocalDate.parse("2020-08-31"));
		when(localDateMapper.fromStringToLocalTime("10:00:00")).thenReturn(LocalTime.parse("10:00:00"));
		when(localDateMapper.fromStringToLocalTime("12:00:00")).thenReturn(LocalTime.parse("12:00:00"));

	}

	private void setupContextValid() {
		LocalDate apbDate = LocalDate.parse("2020-08-12");
		LocalTime apbHour = LocalTime.parse("11:15:00");
		UpdateDiaryAppointmentBo apb1 = new UpdateDiaryAppointmentBo(1, apbDate, apbHour, (short) 1, (short) 1, false, null, null, null);
		List<UpdateDiaryAppointmentBo> returnFutureAppmets = Stream.of(apb1).collect(Collectors.toList());
		when(diaryPort.getUpdateDiaryAppointments(any())).thenReturn(returnFutureAppmets);
		DiaryBo diaryBo = getDiaryBo();
		when(diaryService.getDiaryById(any())).thenReturn(diaryBo);
	}

	private void setupContextInvalid() {
		LocalDate apbDate = LocalDate.parse("2020-08-12");
		LocalTime apbHour = LocalTime.parse("13:15:00");
		UpdateDiaryAppointmentBo apb1 = new UpdateDiaryAppointmentBo(1, apbDate, apbHour, (short) 1, (short) 2, false, null, null, null);
		List<UpdateDiaryAppointmentBo> returnFutureAppmets = Stream.of(apb1).collect(Collectors.toList());
		when(diaryPort.getUpdateDiaryAppointments(any())).thenReturn(returnFutureAppmets);
		DiaryBo diaryBo = getDiaryBo();
		when(diaryService.getDiaryById(any())).thenReturn(diaryBo);
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

	private DiaryBo getDiaryBo() {
		DiaryBo diary = new DiaryBo();
		diary.setAppointmentDuration((short) 20);
		diary.setAutomaticRenewal(false);
		DiaryOpeningHoursBo doh = new DiaryOpeningHoursBo();
		doh.setMedicalAttentionTypeId((short) 1);
		doh.setOverturnCount((short) 2);
		OpeningHoursBo oh = new OpeningHoursBo();
		oh.setDayWeekId((short) 3);
		oh.setFrom(LocalTime.parse("10:00:00"));
		oh.setTo(LocalTime.parse("12:00:00"));
		doh.setOpeningHours(oh);
		diary.setDiaryOpeningHours(Stream.of(doh).collect(Collectors.toList()));
		diary.setHealthcareProfessionalId(1);
		diary.setDoctorsOfficeId(1);
		diary.setStartDate(LocalDate.parse("2020-08-01"));
		diary.setEndDate(LocalDate.parse("2020-08-29"));
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
		setupContextInvalid();
		when(contextMock.buildConstraintViolationWithTemplate(any())).thenReturn(mockConstraintViolationBuilder);
		Assertions.assertFalse(diaryEmptyAppointmentsValidator.isValid(getDiaryDto(), contextMock));
	}
}
