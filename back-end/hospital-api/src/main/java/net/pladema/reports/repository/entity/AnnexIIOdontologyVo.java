package net.pladema.reports.repository.entity;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AnnexIIOdontologyVo {

	private String speciality;
	private String diagnostics;
	private Boolean hasProcedures;

	public AnnexIIOdontologyVo(String speciality, String diagnostics, Boolean hasProcedures) {
		this.speciality = speciality;
		this.diagnostics = diagnostics;
		this.hasProcedures = hasProcedures;
	}
}
