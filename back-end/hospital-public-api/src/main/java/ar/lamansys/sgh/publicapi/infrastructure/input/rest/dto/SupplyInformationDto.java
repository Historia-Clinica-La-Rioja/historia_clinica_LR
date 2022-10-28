package ar.lamansys.sgh.publicapi.infrastructure.input.rest.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@Builder
public class SupplyInformationDto implements Serializable {

	private String supplyType;
	private String status;
	private SnomedDto snomed;
	private LocalDateTime administrationTime;
}