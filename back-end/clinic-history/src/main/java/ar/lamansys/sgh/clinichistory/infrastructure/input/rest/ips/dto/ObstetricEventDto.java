package ar.lamansys.sgh.clinichistory.infrastructure.input.rest.ips.dto;

import ar.lamansys.sgh.clinichistory.domain.ips.enums.EPregnancyTermination;
import ar.lamansys.sgx.shared.dates.controller.dto.DateDto;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.annotation.Nullable;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import java.util.List;


@Getter
@Setter
@ToString
public class ObstetricEventDto {

	@Nullable
	private Short previousPregnancies;
	@Nullable
	private DateDto currentPregnancyEndDate;
	@Nullable @Min(0) @Max(43)
	private Short gestationalAge;
	@Nullable
	private EPregnancyTermination pregnancyTerminationType;
	@NotNull
	private List<NewbornDto> newborns;

}
