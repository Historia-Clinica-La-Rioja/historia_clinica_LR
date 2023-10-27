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
public class QuestionnaireSummary {

	private String title;

	private String professionalName;

	private String professionalMiddleName;

	private String professionalLastName;

	private String licenseNumber;

	private String createdOn;

	public QuestionnaireSummary(String name, String middleName, String lastName, String licenseNumber, String date){
		this.professionalName = name;
		this.professionalMiddleName = middleName;
		this.professionalLastName = lastName;
		this.licenseNumber = licenseNumber;
		this.createdOn = date;
	}
}
