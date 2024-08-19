package ar.lamansys.sgh.publicapi.reports.infrastructure.input.rest.dto;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class MedicalCoverageNameDto {
	String name;
	Integer rnos;
}
