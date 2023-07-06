package net.pladema.medicalconsultation.appointment.repository.domain;


import lombok.ToString;
import lombok.Value;

@Value
@ToString
public class AppointmentOrderImageExistCheckVo {

	private final Integer orderId;

	private final String documentStatus;

	private final Integer appointmentId;


	public AppointmentOrderImageExistCheckVo(Integer appointmentId, String documentStatus) {
		this.appointmentId = appointmentId;
		this.documentStatus = documentStatus;
		this.orderId = null;
	}
}
