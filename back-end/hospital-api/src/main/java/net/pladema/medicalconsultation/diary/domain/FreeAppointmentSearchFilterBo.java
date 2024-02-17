package net.pladema.medicalconsultation.diary.domain;

import lombok.Getter;
import lombok.Setter;
import net.pladema.medicalconsultation.appointment.domain.enums.EAppointmentModality;

import java.time.LocalDate;

@Getter
@Setter
public class FreeAppointmentSearchFilterBo {

	private EAppointmentModality modality;

	private boolean mustBeProtected;

	private LocalDate date;

}
