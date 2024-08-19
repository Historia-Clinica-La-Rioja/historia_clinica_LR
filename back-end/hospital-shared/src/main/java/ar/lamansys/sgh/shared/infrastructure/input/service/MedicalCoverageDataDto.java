package ar.lamansys.sgh.shared.infrastructure.input.service;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class MedicalCoverageDataDto {

	private String name;
	private String cuit;
	private String type;
	private String rnos;

}
