package ar.lamansys.sgh.publicapi.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.time.LocalDateTime;

@Getter
@Builder
@ToString
public class DocumentInfoBo {

	private final Long id;
	private final String filePath;
	private final String type;
	private final String fileName;
	private final LocalDateTime updateOn;
}

