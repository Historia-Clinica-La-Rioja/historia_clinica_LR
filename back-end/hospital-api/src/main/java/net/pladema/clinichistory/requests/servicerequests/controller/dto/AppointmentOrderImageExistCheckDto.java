package net.pladema.clinichistory.requests.servicerequests.controller.dto;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import net.pladema.medicalconsultation.appointment.repository.domain.AppointmentOrderImageExistCheckBo;

@Getter
@Setter
@ToString
public class AppointmentOrderImageExistCheckDto implements Serializable {

	private Boolean hasActiveAppointment;

	private Boolean documentStatus;

	private Integer appointmentId;


	public AppointmentOrderImageExistCheckDto(AppointmentOrderImageExistCheckBo appointmentOrderImageExistCheckBo) {
		this.hasActiveAppointment = appointmentOrderImageExistCheckBo.getHasActiveAppointment();
		this.documentStatus = appointmentOrderImageExistCheckBo.getIsReportCompleted();
		this.appointmentId = appointmentOrderImageExistCheckBo.getAppointmentId();
	}
}
