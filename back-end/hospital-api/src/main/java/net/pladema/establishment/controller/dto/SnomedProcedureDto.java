package net.pladema.establishment.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SnomedProcedureDto {

	private Long id;

	private Long conceptId;

	private Integer groupId;

	private final String groupDescription = "PROCEDURE";

	private String conceptSctid;

	private String conceptPt;

}
