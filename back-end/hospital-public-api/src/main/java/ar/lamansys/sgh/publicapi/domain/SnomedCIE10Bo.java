package ar.lamansys.sgh.publicapi.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class SnomedCIE10Bo {
	private String sctId;
	private String pt;
	private String cie10Id;
}
