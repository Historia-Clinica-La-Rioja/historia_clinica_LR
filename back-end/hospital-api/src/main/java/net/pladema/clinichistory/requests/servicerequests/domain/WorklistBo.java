package net.pladema.clinichistory.requests.servicerequests.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@AllArgsConstructor
public class WorklistBo {

	private Integer patientId;

	private Short patientIdentificationTypeId;

	private String patientIdentificationNumber;

	private String patientFirstName;

	private String patientMiddleNames;

	private String patientLastName;

	private String patientOtherNames;

	private String patientNameSelfDetermiantion;

	private String patientFullName;

	private LocalDateTime actionTime;

	private Short statusId;

	private Integer appointmentId;

	public WorklistBo(Integer patientId, Short patientIdentificationTypeId, String patientIdentificationNumber, String patientFirstName, String patientMiddleNames, String patientLastName, String patientOtherNames, String patientNameSelfDetermiantion, Integer appointmentId, LocalDateTime actionTime) {
		this.patientId = patientId;
		this.patientIdentificationTypeId = patientIdentificationTypeId;
		this.patientIdentificationNumber = patientIdentificationNumber;
		this.patientFirstName = patientFirstName;
		this.patientMiddleNames = patientMiddleNames;
		this.patientLastName = patientLastName;
		this.patientOtherNames = patientOtherNames;
		this.patientNameSelfDetermiantion = patientNameSelfDetermiantion;
		this.appointmentId = appointmentId;
		this.actionTime = actionTime;
	}


	public String getFullName(boolean ffIsOn) {

		String fullName;

		if (ffIsOn && this.patientNameSelfDetermiantion != null) {
			fullName = this.patientNameSelfDetermiantion + " " + this.patientLastName;
		} else {
			fullName = this.patientFirstName;
			if (this.patientMiddleNames != null) {
				fullName += " " + this.patientMiddleNames;
			}
			fullName += " " + this.patientLastName;
		}
		if (this.patientOtherNames != null) {
			fullName += " " + this.patientOtherNames;
		}
		return fullName;
	}
}
