package net.pladema.access.domain.bo;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import net.pladema.access.infrastructure.input.dto.ClinicHistoryAccessDto;

@AllArgsConstructor
@Getter
@Setter
@ToString
public class ClinicHistoryAccessBo {

	private String observations;
	private Short reasonId;
	private Short scope;

	public ClinicHistoryAccessBo(ClinicHistoryAccessDto dto) {
		this.observations = dto.getObservations();
		this.reasonId = dto.getReason().getId();
		this.scope = dto.getScope();
	}
}
