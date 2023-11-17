package net.pladema.establishment.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SnomedProblemDto {

	private Long id;

	private Long conceptId;

	private Integer groupId;

	private final String groupDescription = "DIAGNOSIS";

	private String conceptSctid;

	private String conceptPt;

}
