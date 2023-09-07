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
	private String category;
	private String templateText;

	@JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
	public DocumentTemplateDto(
			@JsonProperty("name") String name,
			@JsonProperty("category") String category,
			@JsonProperty("templateText") String templateText) {

		this.name = name;
		this.category = category;
		this.templateText = templateText;
	}

}
