package net.pladema.access.infrastructure.input.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import net.pladema.access.domain.enums.EClinicHistoryAccessReason;

@AllArgsConstructor
@Getter
public class ClinicHistoryAccessDto {

	private EClinicHistoryAccessReason eClinicHistoryAccessReason;
}
