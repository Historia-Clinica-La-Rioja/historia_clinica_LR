package net.pladema.medicalconsultation.appointment.service.impl;

import net.pladema.medicalconsultation.appointment.service.AppointmentService;
import net.pladema.medicalconsultation.diary.service.DiaryService;

import net.pladema.medicalconsultation.diary.service.exception.DiaryNotFoundException;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

@ExtendWith(MockitoExtension.class)
class AppointmentDailyAmountServiceImplTest {

	@Mock
	private DiaryService diaryService;
	@Mock
	private AppointmentService appointmentService;

	private AppointmentDailyAmountServiceImpl appointmentDailyAmountService;

	@BeforeEach
	void setUp() {
		this.appointmentDailyAmountService = new AppointmentDailyAmountServiceImpl(appointmentService, diaryService);
	}

	@Test
	void getDailyAmountsWithNonExistentDiary() {
		Mockito.when(diaryService.getDiary(1)).thenReturn(
				Optional.empty()
		);
		var exception = Assertions.assertThrows(DiaryNotFoundException.class, () -> {
			appointmentDailyAmountService.getDailyAmounts(1);
		});
		Assertions.assertEquals(exception.getMessage(), "La Agenda solicitada no existe");
	}
}