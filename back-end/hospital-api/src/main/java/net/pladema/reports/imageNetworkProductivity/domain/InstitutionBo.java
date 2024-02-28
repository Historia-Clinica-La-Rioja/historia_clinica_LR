package net.pladema.reports.imageNetworkProductivity.domain;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class InstitutionBo {

	private String name;

	private String departmentName;

	private String sisaCode;

	private String stateName;

	public InstitutionBo(String name, String departmentName, String sisaCode, String stateName) {
		this.name = name.toUpperCase();
		this.departmentName = departmentName.toUpperCase();
		this.sisaCode = sisaCode;
		this.stateName = stateName.toUpperCase();
	}

}
