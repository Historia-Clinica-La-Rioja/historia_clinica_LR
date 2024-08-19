package ar.lamansys.sgh.clinichistory.infrastructure.input.rest.ips.dto;


import ar.lamansys.sgh.clinichistory.infrastructure.input.rest.hce.dto.HCEHealthcareProfessionalDto;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.document.entity.EProfessionType;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.annotation.Nullable;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@NoArgsConstructor
public class DocumentHealthcareProfessionalDto {

	@Nullable
	private Integer id;

	@NotNull
	private HCEHealthcareProfessionalDto healthcareProfessional;

	@NotNull
	private ProfessionTypeDto profession;

	@Nullable
	private String comments;

}
