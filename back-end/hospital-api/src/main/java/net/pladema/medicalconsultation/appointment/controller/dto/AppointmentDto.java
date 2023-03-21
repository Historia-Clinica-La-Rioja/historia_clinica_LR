package net.pladema.medicalconsultation.appointment.controller.dto;


import javax.annotation.Nullable;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class AppointmentDto extends CreateAppointmentDto {

    private Short appointmentStateId;

    @Nullable
    private String stateChangeReason;

	@Nullable
	private String observation;

	@Nullable
	private String observationBy;

	private boolean isProtected;
}
