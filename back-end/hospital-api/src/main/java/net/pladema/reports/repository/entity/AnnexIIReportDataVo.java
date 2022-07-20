package net.pladema.reports.repository.entity;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AnnexIIReportDataVo {

	private String speciality;
	private String diagnostics;
	private Boolean hasProcedures;

	public AnnexIIReportDataVo(String speciality, String diagnostics, Boolean hasProcedures) {
		this.speciality = speciality;
		this.diagnostics = diagnostics;
		this.hasProcedures = hasProcedures;
	}

	@Override
	public boolean equals(Object obj) {
		if(obj != null)
			return this.diagnostics.equals(((AnnexIIReportDataVo) obj).getDiagnostics());
		else return false;
	}

}
