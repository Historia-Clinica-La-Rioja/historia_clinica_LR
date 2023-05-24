package net.pladema.medicalconsultation.appointment.service.domain;

import lombok.*;

@Getter
@Setter
@EqualsAndHashCode
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class AppointmentOrderImageBo {

    private Integer appointmentId;

    private Integer orderId;

	private Integer transcribedOrderId;

	private Integer studyId;

    private boolean completed;

    private String imageId;

	//Asocia orden estandar al turno
	public AppointmentOrderImageBo(Integer appointmentId, Integer orderId, Integer studyId, Boolean completed, String imageId){
		this.appointmentId = appointmentId;
		this.orderId = orderId;
		this.studyId = studyId;
		this.completed = completed;
		this.imageId = imageId;
	}

	//Asocia orden transcripta al turno
	public AppointmentOrderImageBo(Integer appointmentId, Boolean completed, String imageId, Integer transcribedOrderId){
		this.appointmentId = appointmentId;
		this.completed = completed;
		this.imageId = imageId;
		this.transcribedOrderId = transcribedOrderId;
	}
}
