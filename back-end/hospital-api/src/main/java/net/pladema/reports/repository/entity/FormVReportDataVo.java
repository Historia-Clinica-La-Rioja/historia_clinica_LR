package net.pladema.reports.repository.entity;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FormVReportDataVo {

	private String diagnostics;
	private String cie10Codes;

	public FormVReportDataVo(String diagnostics, String cie10Codes) {
		this.diagnostics = diagnostics;
		this.cie10Codes = cie10Codes;
	}
}
