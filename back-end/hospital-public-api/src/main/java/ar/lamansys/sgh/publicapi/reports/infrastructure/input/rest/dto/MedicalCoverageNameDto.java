package ar.lamansys.sgh.publicapi.reports.infrastructure.input.rest.dto;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@Builder
@Getter
@EqualsAndHashCode
public class MedicalCoverageNameDto {
	String name;
	Integer rnos;
}
