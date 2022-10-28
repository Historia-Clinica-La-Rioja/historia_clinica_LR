package ar.lamansys.sgh.publicapi.infrastructure.input.rest.dto;

import ar.lamansys.sgx.shared.dates.controller.dto.DateTimeDto;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@Builder
@ToString
public class DocumentInfoDto {

	private final Long id;

	private final String filePath;

	private final String type;

	private final String fileName;

	private final DateTimeDto updateOn;
}
