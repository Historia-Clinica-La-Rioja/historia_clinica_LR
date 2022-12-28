package net.pladema.medicalconsultation.appointment.service.domain;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import net.pladema.medicalconsultation.diary.service.domain.CustomRecurringAppointmentBo;

@Getter
@Setter
@EqualsAndHashCode
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class CreateCustomAppointmentBo {

	private AppointmentBo createAppointmentBo;

	private CustomRecurringAppointmentBo customRecurringAppointmentBo;
}
