package net.pladema.patient.service.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import net.pladema.patient.controller.dto.EItsCoveredType;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class ItsCoveredResponseBo {

	private String message;
	private Short covered;

	public ItsCoveredResponseBo(EItsCoveredType EItsCoveredType) {
		setItsCovered(EItsCoveredType);
	}

	public void setItsCovered(EItsCoveredType EItsCoveredType) {
		this.message = EItsCoveredType.getDescription();
		this.covered = EItsCoveredType.getId();
	}
}
