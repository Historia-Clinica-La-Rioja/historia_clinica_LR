package net.pladema.template.infrastructure.input.rest.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;


@Getter
@Setter
@ToString
@NoArgsConstructor
public class DocumentTemplateDto {

	private String name;
	private Short typeId;
	private String templateText;

	@JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
	public DocumentTemplateDto(
			@JsonProperty("name") String name,
			@JsonProperty("typeId") Short typeId,
			@JsonProperty("templateText") String templateText) {

		this.name = name;
		this.typeId = typeId;
		this.templateText = templateText;
	}

}
