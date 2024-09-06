package ar.lamansys.sgh.publicapi.reports.infrastructure.input.rest.dto;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@Builder
@Getter
@EqualsAndHashCode
public class ProfessionalDataDto {
	private Integer id;
	private String identificationType;
	private String identificationNumber;
	private String cuil;
	private String lastName;
	private String middleNames;
	private String firstName;
	private String selfPerceivedName;
}
