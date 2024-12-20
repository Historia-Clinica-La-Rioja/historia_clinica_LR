package net.pladema.medicalconsultation.appointment.service.impl;

import net.pladema.medicalconsultation.appointment.service.AppointmentService;
import net.pladema.medicalconsultation.diary.service.DiaryService;

import net.pladema.medicalconsultation.diary.service.HolidaysService;
import net.pladema.medicalconsultation.diary.service.exception.DiaryNotFoundException;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
class AppointmentDailyAmountServiceImplTest {

	@Mock
	DiaryService diaryService;
	@Mock
	AppointmentService appointmentService;

	@Mock
	HolidaysService holidaysService;

	@InjectMocks
	AppointmentDailyAmountServiceImpl appointmentDailyAmountService;

	@Test
	void getDailyAmountsWithNonExistentDiary() {
		Mockito.when(diaryService.getDiary(1)).thenReturn(
				Optional.empty()
		);
		var exception = Assertions.assertThrows(DiaryNotFoundException.class, () -> {
			appointmentDailyAmountService.getDailyAmounts(1, LocalDate.now(),LocalDate.now());
		});
		Assertions.assertEquals(exception.getMessage(), "La agenda solicitada no existe");
	}
}
