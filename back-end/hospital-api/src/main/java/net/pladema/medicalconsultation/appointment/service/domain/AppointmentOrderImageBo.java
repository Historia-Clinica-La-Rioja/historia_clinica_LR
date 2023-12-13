package net.pladema.medicalconsultation.appointment.service.domain;

import lombok.*;
import net.pladema.clinichistory.requests.servicerequests.infrastructure.input.service.EDiagnosticImageReportStatus;

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

	private Integer destInstitutionId;

	private Short reportStatusId;

	//Asocia orden estandar al turno
	public AppointmentOrderImageBo(Integer appointmentId, Integer orderId, Integer studyId, Boolean completed, String imageId, Integer institutionId){
		this.appointmentId = appointmentId;
		this.orderId = orderId;
		this.studyId = studyId;
		this.completed = completed;
		this.imageId = imageId;
		this.destInstitutionId = institutionId;
		this.reportStatusId = EDiagnosticImageReportStatus.PENDING.getId();
	}

	//Asocia orden transcripta al turno
	public AppointmentOrderImageBo(Integer appointmentId, Boolean completed, String imageId, Integer transcribedOrderId, Integer institutionId){
		this.appointmentId = appointmentId;
		this.completed = completed;
		this.imageId = imageId;
		this.transcribedOrderId = transcribedOrderId;
		this.destInstitutionId = institutionId;
		this.reportStatusId = EDiagnosticImageReportStatus.PENDING.getId();
	}
}
