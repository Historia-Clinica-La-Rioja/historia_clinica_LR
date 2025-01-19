package net.pladema.parameterizedform.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class ParameterizedFormBo {

	private Integer id;
	private String name;
	private Short statusId;
	private Boolean outpatientEnabled;
	private Boolean internmentEnabled;
	private Boolean emergencyCareEnabled;
	private Boolean isDomain;
	private Integer institutionId;
	private Boolean isEnabled;

}
