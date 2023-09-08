package net.pladema.medicalconsultation.appointment.repository.domain;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AppointmentOrderImageExistCheckBo {

	public static final String FINAL = "445665009";

	private Boolean hasActiveAppointment;

	private Boolean isReportCompleted;

	private Integer appointmentId;

	public AppointmentOrderImageExistCheckBo(AppointmentOrderImageExistCheckVo appointmentOrderImageExistCheckVo) {
		if (appointmentOrderImageExistCheckVo != null){
			this.isReportCompleted = appointmentOrderImageExistCheckVo.getDocumentStatus() != null ? isCompleted(appointmentOrderImageExistCheckVo.getDocumentStatus()) : false;
			this.appointmentId = appointmentOrderImageExistCheckVo.getAppointmentId();
			this.hasActiveAppointment = appointmentOrderImageExistCheckVo.getDiagnosticReportId() != null ? true : false;
		}
		else {
			this.isReportCompleted = false;
			this.hasActiveAppointment = false;
			this.appointmentId = null;
		}
	}
	private Boolean isCompleted(String id){
		if (id.equals(FINAL))
			return true;
		else return false;
	}
}
