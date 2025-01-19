package ar.lamansys.sgh.publicapi.reports.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class ClinicalSpecialtyBo {
	private Integer id;
	private String description;
	private String snomedId;
}
