package ar.lamansys.sgh.publicapi.reports.infrastructure.input.rest.dto;

import java.time.LocalDate;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@Builder
@Getter
@EqualsAndHashCode
public class ConsultationItemWithDateDto {
	private String sctId;
	private String pt;
	private String cie10Id;
	private LocalDate startDate;
	private LocalDate endDate;
}
