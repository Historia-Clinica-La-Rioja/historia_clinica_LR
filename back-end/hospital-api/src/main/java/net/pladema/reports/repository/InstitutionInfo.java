package net.pladema.reports.repository;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@AllArgsConstructor
public class InstitutionInfo {

	private String province;

	private String department;

	private String sisaCode;

	private String institution;

}
