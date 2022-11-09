package ar.lamansys.sgx.shared.files.infrastructure.input.rest.backoffice.dto;

import java.time.ZonedDateTime;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class FileInfoDto {

	private Long id;

	private String name;

	private String relativePath;

	private String originalPath;
	private String uuidfile;

	private String contentType;

	private long size;

	private String source;

	private String generatedBy;
	private FileCreationDto creationable;

	public FileInfoDto(Long id, String name, String relativePath, String originalPath, String uuidfile, String contentType,
					   long size, String source, String generatedBy, ZonedDateTime createdOn) {
		this.id = id;
		this.name = name;
		this.relativePath = relativePath;
		this.originalPath = originalPath;
		this.uuidfile = uuidfile;
		this.contentType = contentType;
		this.size = size;
		this.source = source;
		this.generatedBy = generatedBy;
		this.creationable = new FileCreationDto(createdOn);
	}

	public ZonedDateTime getCreatedOn() {
		if (creationable != null)
			return creationable.getCreatedOn();
		return null;
	}
}
