package ar.lamansys.sgh.publicapi.reports.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class IdentificationBo {
	private String identificationType;
	private String identificationNumber;
}
