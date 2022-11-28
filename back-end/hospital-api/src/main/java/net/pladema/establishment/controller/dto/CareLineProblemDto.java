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

	private String conceptSctid;

}
