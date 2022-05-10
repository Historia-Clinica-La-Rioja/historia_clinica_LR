package net.pladema.reports.repository.entity;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FormVOdontologyVo {

	private String diagnostics;
	private String cie10Codes;

	public FormVOdontologyVo(String diagnostics, String cie10Codes) {
		this.diagnostics = diagnostics;
		this.cie10Codes = cie10Codes;
	}
}
