package net.pladema.reports.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class AppointmentConsultationSummaryBo {

	private String hierarchicalUnitType;
	private String hierarchicalUnitAlias;
	private String clinicalSpecialty;

	private Integer ageRange0to1F;
	private Integer ageRange0to1M;
	private Integer ageRange0to1X;

	private Integer ageRange1to4F;
	private Integer ageRange1to4M;
	private Integer ageRange1to4X;

	private Integer ageRange5to9F;
	private Integer ageRange5to9M;
	private Integer ageRange5to9X;

	private Integer ageRange10to14F;
	private Integer ageRange10to14M;
	private Integer ageRange10to14X;

	private Integer ageRange15to19F;
	private Integer ageRange15to19M;
	private Integer ageRange15to19X;

	private Integer ageRange20to34F;
	private Integer ageRange20to34M;
	private Integer ageRange20to34X;

	private Integer ageRange35to49F;
	private Integer ageRange35to49M;
	private Integer ageRange35to49X;

	private Integer ageRange50to64F;
	private Integer ageRange50to64M;
	private Integer ageRange50to64X;

	private Integer ageRangeOver65F;
	private Integer ageRangeOver65M;
	private Integer ageRangeOver65X;

	private Integer unspecified;
	private Integer total;
	private Integer hasCoverage;
	private Integer noCoverage;

	public List<Integer> getAllAgeRanges() {
		return List.of(
				ageRange0to1M, ageRange0to1F, ageRange0to1X,
				ageRange1to4M, ageRange1to4F, ageRange1to4X,
				ageRange5to9M, ageRange5to9F, ageRange5to9X,
				ageRange10to14M, ageRange10to14F, ageRange10to14X,
				ageRange15to19M, ageRange15to19F, ageRange15to19X,
				ageRange20to34M, ageRange20to34F, ageRange20to34X,
				ageRange35to49M, ageRange35to49F, ageRange35to49X,
				ageRange50to64M, ageRange50to64F, ageRange50to64X,
				ageRangeOver65M, ageRangeOver65F, ageRangeOver65X
		);
	}

}
