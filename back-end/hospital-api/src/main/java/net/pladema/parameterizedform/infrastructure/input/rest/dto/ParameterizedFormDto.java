package net.pladema.parameterizedform.infrastructure.input.rest.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@AllArgsConstructor
@RequiredArgsConstructor
public class ParameterizedFormDto {

	private Integer id;
	private String name;
	private Short statusId;
	private Boolean outpatientEnabled;
	private Boolean internmentEnabled;
	private Boolean emergencyCareEnabled;
	private Boolean isDomain;
	private Integer institutionId;
	private Boolean isEnabled;

	public ParameterizedFormDto(Integer id, String name, Short statusId, Boolean outpatientEnabled, Boolean internmentEnabled, Boolean emergencyCareEnabled) {
		this.id = id;
		this.name = name;
		this.statusId = statusId;
		this.outpatientEnabled = outpatientEnabled;
		this.internmentEnabled = internmentEnabled;
		this.emergencyCareEnabled = emergencyCareEnabled;
	}

}