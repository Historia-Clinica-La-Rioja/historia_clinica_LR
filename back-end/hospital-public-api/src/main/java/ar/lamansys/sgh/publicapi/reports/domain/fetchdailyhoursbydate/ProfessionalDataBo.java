package ar.lamansys.sgh.publicapi.reports.domain.fetchdailyhoursbydate;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class ProfessionalDataBo {
	private Integer id;
	private String identificationType;
	private String identificationNumber;
	private String cuil;
	private String lastName;
	private String middleNames;
	private String firstName;
	private String selfPerceivedName;
}
