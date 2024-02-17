package net.pladema.access.infrastructure.input.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import net.pladema.access.domain.enums.EClinicHistoryAccessReason;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class ClinicHistoryAccessDto {

	private String observations;
	private EClinicHistoryAccessReason reason;
	private Short scope;
}
