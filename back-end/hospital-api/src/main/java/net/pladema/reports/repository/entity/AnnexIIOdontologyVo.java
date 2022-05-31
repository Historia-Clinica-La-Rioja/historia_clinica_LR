package net.pladema.reports.repository.entity;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AnnexIIOdontologyVo {

	private String speciality;
	private Boolean hasProcedures;

	public AnnexIIOdontologyVo(String speciality, Boolean hasProcedures) {
		this.speciality = speciality;
		this.hasProcedures = hasProcedures;
	}
}
