package ar.lamansys.sgh.publicapi.domain;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class SupplyInformationBo {
	private final String supplyType;
	private final String status;
	private final SnomedBo snomedBo;
	private final LocalDateTime administrationTime;

}
