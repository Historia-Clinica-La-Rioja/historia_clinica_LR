package net.pladema.clinichistory.requests.servicerequests.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import net.pladema.establishment.service.domain.InstitutionBasicInfoBo;

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

	private InstitutionBasicInfoBo completionInstitution;

	public WorklistBo(Integer patientId, Short patientIdentificationTypeId, String patientIdentificationNumber, String patientFirstName, String patientMiddleNames, String patientLastName, String patientOtherNames, String patientNameSelfDetermiantion, Integer appointmentId, LocalDateTime actionTime,
					  Integer completionInstitutionId, String completionInstitutionName) {
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
		this.completionInstitution = new InstitutionBasicInfoBo(completionInstitutionId, completionInstitutionName);
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
