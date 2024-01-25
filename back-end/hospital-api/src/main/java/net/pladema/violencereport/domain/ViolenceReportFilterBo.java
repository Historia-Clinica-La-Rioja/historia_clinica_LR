package net.pladema.violencereport.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class ViolenceReportFilterBo {

	private Short situationId;

	private Integer modalityId;

	private Integer typeId;

	private Integer institutionId;

}
