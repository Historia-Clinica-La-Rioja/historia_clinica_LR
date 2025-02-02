package net.pladema.medicalconsultation.appointment.controller.dto;

import ar.lamansys.sgx.shared.dates.controller.dto.DateTimeDto;
import lombok.*;
import net.pladema.medicalconsultation.appointment.domain.enums.EAppointmentModality;

import javax.annotation.Nullable;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@EqualsAndHashCode
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class UpdateAppointmentDateDto {

	@NotNull
	private Integer appointmentId;

	@NotNull
	private DateTimeDto date;

	@NotNull
	private Integer openingHoursId;

	@NotNull
	private EAppointmentModality modality;

	@Nullable
	private String patientEmail;

	@Nullable
	private Short recurringAppointmentTypeId;
}
