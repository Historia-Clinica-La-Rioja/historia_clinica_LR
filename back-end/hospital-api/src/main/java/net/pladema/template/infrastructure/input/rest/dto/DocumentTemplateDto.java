package net.pladema.template.infrastructure.input.rest.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotEmpty;


@Getter
@Setter
@ToString
@NoArgsConstructor
public class DocumentTemplateDto {

	private String name;

	@JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
	public DocumentTemplateDto(
			@NotEmpty(message = "{value.mandatory}") @JsonProperty("name") String name) {

		this.name = name;
	}

}
