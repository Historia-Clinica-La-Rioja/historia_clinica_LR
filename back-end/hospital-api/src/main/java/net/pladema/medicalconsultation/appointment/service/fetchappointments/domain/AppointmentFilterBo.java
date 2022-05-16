package net.pladema.medicalconsultation.appointment.service.fetchappointments.domain;

import java.time.LocalDate;
import java.util.List;

import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class AppointmentFilterBo {

	@ToString.Include
	private final String sisaCode;
	@ToString.Include
	private final String identificationNumber;

	@ToString.Include
	private final List<Short> includeAppointmentStatus;
	@ToString.Include
	private final LocalDate startDate;
	@ToString.Include
	private final LocalDate endDate;

	public AppointmentFilterBo(String sisaCode, String identificationNumber, List<Short> includeAppointmentStatus,
							   LocalDate startDate, LocalDate endDate) {
		this.sisaCode = sisaCode;
		this.identificationNumber = identificationNumber;
		this.includeAppointmentStatus = includeAppointmentStatus;
		this.startDate = startDate;
		this.endDate = endDate;
	}

	public boolean hasAppointmentStatus() {
		return (includeAppointmentStatus != null && !includeAppointmentStatus.isEmpty());
	}
}
