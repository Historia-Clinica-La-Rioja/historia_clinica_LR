package ar.lamansys.sgh.clinichistory.infrastructure.output.repository.backoffice.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.time.ZonedDateTime;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class DocumentFileDto {

	private Long id;

	private Integer sourceId;

	private Short sourceTypeId;

	private Short typeId;

	private String filename;

	private CreationableDto creationable;

	public DocumentFileDto(Long documentId, Integer sourceId, Short sourceTypeId, Short documentType,
                           String filename, ZonedDateTime createdOn){
		this.id = documentId;
		this.sourceId = sourceId;
		this.sourceTypeId = sourceTypeId;
		this.typeId = documentType;
		this.filename = filename;
		this.creationable = new CreationableDto(createdOn);
	}

	public ZonedDateTime getCreatedOn() {
		if (creationable != null)
			return creationable.getCreatedOn();
		return null;
	}
}
