package net.pladema.establishment.controller.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class CareLineProblemDto {

	private Integer id;

	private Integer careLineId;

	private Integer snomedId;

	private Long conceptId;

	public CareLineProblemDto(Integer id, Integer careLineId, Integer snomedId) {
		this.id = id;
		this.careLineId = careLineId;
		this.snomedId = snomedId;
	}

}
