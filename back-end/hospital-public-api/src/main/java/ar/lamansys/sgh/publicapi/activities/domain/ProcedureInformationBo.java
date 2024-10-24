package ar.lamansys.sgh.publicapi.activities.domain;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class ProcedureInformationBo {
	private final SnomedBo snomedBo;
	private final LocalDateTime administrationTime;
}
