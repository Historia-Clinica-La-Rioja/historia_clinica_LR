package net.pladema.patient.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import net.pladema.patient.service.domain.ItsCoveredResponseBo;

@Getter
@Setter
@AllArgsConstructor
@ToString
public class ItsCoveredResponseDto {

	private String message;
	private Short covered;

	public ItsCoveredResponseDto(ItsCoveredResponseBo itsCoveredResponseBo) {
		this.message = itsCoveredResponseBo.getMessage();
		this.covered = itsCoveredResponseBo.getCovered();
	}
}
