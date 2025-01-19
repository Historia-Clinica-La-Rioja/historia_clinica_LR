package ar.lamansys.sgh.clinichistory.infrastructure.input.rest.ips.dto;

import ar.lamansys.sgh.clinichistory.domain.ips.enums.EEventLocation;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.masterdata.entity.EExternalCauseType;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.annotation.Nullable;
import javax.validation.Valid;

@Getter
@Setter
@ToString
public class ExternalCauseDto {

	@Nullable
	private Integer id;

	@Nullable
	private EExternalCauseType externalCauseType;

	@Nullable
	private EEventLocation eventLocation;

	@Nullable
	@Valid
	private SnomedDto snomed;

}
