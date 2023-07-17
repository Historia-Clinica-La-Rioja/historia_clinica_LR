package net.pladema.medicalconsultation.appointment.repository.domain;

import lombok.Getter;
import lombok.ToString;
import net.pladema.establishment.service.domain.InstitutionBasicInfoBo;
import net.pladema.medicalconsultation.appointment.repository.entity.Appointment;

import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@ToString
public class EquipmentAppointmentVo {

	private final Appointment appointment;

	private final Short identificationType;

	private final String identificationnumber;

	private final InstitutionBasicInfoBo institutionBasicInfoBo;

	private final Short reportStatusId;

	public EquipmentAppointmentVo(Appointment appointment, Short identificationType, String identificationnumber, Short reportStatusId){
		this.appointment = appointment;
		this.identificationType = identificationType;
		this.identificationnumber = identificationnumber;
		this.reportStatusId = reportStatusId;
		this.institutionBasicInfoBo = null;
	}

	public EquipmentAppointmentVo(Appointment appointment, Short identificationType, String identificationnumber, Integer institutionId, String institutionName, Short reportStatusId){
		this.appointment = appointment;
		this.identificationType = identificationType;
		this.identificationnumber = identificationnumber;
		this.reportStatusId = reportStatusId;
		this.institutionBasicInfoBo = new InstitutionBasicInfoBo(institutionId, institutionName);
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
