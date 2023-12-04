package net.pladema.questionnaires.common.dto;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class QuestionnaireInfo {

	private String createdOn;

	private String institution;

	private String title;

	private String patientAgeWhenAttended;

	private String patientIdNumber;

	private String patientName;

	private String professionalLicenseNumber;

	private String professionalName;

	private String questionnaireId;

}
