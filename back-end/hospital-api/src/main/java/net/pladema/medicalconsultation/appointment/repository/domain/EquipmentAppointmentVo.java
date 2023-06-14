package net.pladema.medicalconsultation.appointment.repository.domain;

import lombok.AllArgsConstructor;
import lombok.ToString;
import lombok.Value;
import net.pladema.establishment.service.domain.InstitutionBasicInfoBo;
import net.pladema.medicalconsultation.appointment.repository.entity.Appointment;

import java.time.LocalDate;
import java.time.LocalTime;

@Value
@ToString
public class EquipmentAppointmentVo {

	private Appointment appointment;

	private Short identificationType;

	private String identificationnumber;

	private InstitutionBasicInfoBo institutionBasicInfoBo;

	public EquipmentAppointmentVo(Appointment appointment, Short identificationType, String identificationnumber){
		this.appointment = appointment;
		this.identificationType = identificationType;
		this.identificationnumber = identificationnumber;
		this.institutionBasicInfoBo = null;
	}

	public EquipmentAppointmentVo(Appointment appointment, Short identificationType, String identificationnumber, Integer institutionId, String institutionName){
		this.appointment = appointment;
		this.identificationType = identificationType;
		this.identificationnumber = identificationnumber;
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
