package net.pladema.medicalconsultation.appointment.repository.domain;

import java.time.LocalDate;
import java.time.LocalTime;
import lombok.Getter;
import lombok.ToString;
import net.pladema.establishment.service.domain.InstitutionBasicInfoBo;
import net.pladema.medicalconsultation.appointment.repository.entity.Appointment;

@Getter
@ToString
public class EquipmentAppointmentVo {

	private final Appointment appointment;

	private final Short identificationType;

	private final String identificationnumber;

	private final InstitutionBasicInfoBo institutionBasicInfoBo;

	private final Short reportStatusId;

	private final Integer serviceRequestId;

	private final Integer transcribedServiceRequestId;

	private final Integer diagnosticReportId;

	public EquipmentAppointmentVo(Appointment appointment, Short identificationType, String identificationnumber, Integer institutionId, String institutionName,
								  Short reportStatusId, Integer diagnosticReportId, Integer serviceRequestId, Integer transcribedServiceRequestId) {
		this.appointment = appointment;
		this.identificationType = identificationType;
		this.identificationnumber = identificationnumber;
		this.reportStatusId = reportStatusId;
		this.institutionBasicInfoBo = new InstitutionBasicInfoBo(institutionId, institutionName);
		this.diagnosticReportId = diagnosticReportId;
		this.serviceRequestId = serviceRequestId;
		this.transcribedServiceRequestId = transcribedServiceRequestId;
	}

	public Integer getId() {
		return appointment.getId();
	}

	public Integer getPatientId() {
		if (appointment == null)
			return null;
		return appointment.getPatientId();
	}
	
	public LocalDate getDate() {
		if (appointment == null)
			return null;
		return appointment.getDateTypeId();
	}

	public LocalTime getHour() {
		if (appointment == null)
			return null;
		return appointment.getHour();
	}

	public Short getAppointmentStateId() {
		if (appointment == null)
			return null;
		return appointment.getAppointmentStateId();
	}

	public boolean isOverturn() {
		if (appointment == null)
			return false;
		return appointment.getIsOverturn();
	}

	public Integer getPatientMedicalCoverageId(){
		if (appointment == null)
			return null;
		return appointment.getPatientMedicalCoverageId();
	}
}
