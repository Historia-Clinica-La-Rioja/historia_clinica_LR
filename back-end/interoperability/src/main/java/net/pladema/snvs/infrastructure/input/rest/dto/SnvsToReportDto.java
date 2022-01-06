package net.pladema.snvs.infrastructure.input.rest.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SnvsToReportDto {

	private SnvsSnomedDto problem;

	private Integer manualClassificationId;

	private Integer groupEventId;

	private Integer eventId;

}
