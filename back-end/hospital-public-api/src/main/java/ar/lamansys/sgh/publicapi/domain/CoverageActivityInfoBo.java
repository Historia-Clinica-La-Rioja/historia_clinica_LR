package ar.lamansys.sgh.publicapi.domain;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class CoverageActivityInfoBo {

	private String affiliateNumber;
	private Boolean attentionCoverage;
	private String cuit;
	private String plan;
}