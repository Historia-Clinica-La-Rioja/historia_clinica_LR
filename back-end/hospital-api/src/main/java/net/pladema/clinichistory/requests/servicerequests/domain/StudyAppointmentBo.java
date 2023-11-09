package net.pladema.clinichistory.requests.servicerequests.domain;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
public class StudyAppointmentBo {

	private Integer patientId;
	private String patientFirstName;
	private String patientMiddleNames;
	private String patientLastName;
	private String patientOtherNames;
	private String patientNameSelfDetermiantion;
	private String patientFullName;
	private LocalDateTime actionTime;
	private Short statusId;
	private InformerObservationBo informerObservations;

	public StudyAppointmentBo(Integer patientId, String patientFirstName, String patientMiddleNames, String patientLastName, String patientOtherNames, String patientNameSelfDetermiantion) {
		this.patientId = patientId;
		this.patientFirstName = patientFirstName;
		this.patientMiddleNames = patientMiddleNames;
		this.patientLastName = patientLastName;
		this.patientOtherNames = patientOtherNames;
		this.patientNameSelfDetermiantion = patientNameSelfDetermiantion;
	}

	public String getFullName(boolean ffIsOn) {

		String fullName = "";

		if (ffIsOn) {
			if (this.patientNameSelfDetermiantion != null)
				fullName = this.patientNameSelfDetermiantion;
			else{
				if (this.patientFirstName != null)
					fullName = this.patientFirstName;
			}
			if (this.patientLastName != null)
				fullName += " " + this.patientLastName;
		} else {
			if (this.patientFirstName != null)
				fullName += " " + this.patientFirstName;
			if (this.patientMiddleNames != null)
				fullName += " " + this.patientMiddleNames;
			if (this.patientLastName != null)
				fullName += " " + this.patientLastName;
		}
		if (this.patientOtherNames != null) {
			fullName += " " + this.patientOtherNames;
		}
		return fullName;
	}
}
