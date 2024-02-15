package net.pladema.clinichistory.documents.domain;

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
public class ClinicalRecordBo {

	private String prefix;
	private String description;
	private String professionalInfo;
	private String createdOn;

	public ClinicalRecordBo (String prefix, String description){
		this.prefix = prefix;
		this.description = description;
	}

}
