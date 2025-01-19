package net.pladema.violencereport.infrastructure.input.rest.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class ViolenceReportFilterDto {

	private Short situationId;

	private Integer modalityId;

	private Integer typeId;

	private Integer institutionId;

}
