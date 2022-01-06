package net.pladema.snvs.infrastructure.input.rest.dto;

import ar.lamansys.sgx.shared.dates.controller.dto.DateDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.annotation.Nullable;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SnvsReportDto {

	private SnvsSnomedDto problem;

	@NotNull
	private Integer groupEventId;

	@NotNull
	private Integer eventId;

	@Nullable
	private Integer manualClassificationId;

	@Nullable
	private String status;

	@Nullable
	private Short responseCode;

	@Nullable
	private Integer sisaRegisteredId;

	@Nullable
	private DateDto lastUpdate;
}


