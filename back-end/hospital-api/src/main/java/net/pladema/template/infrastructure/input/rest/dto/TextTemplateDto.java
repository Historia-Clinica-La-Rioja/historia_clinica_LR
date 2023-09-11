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
public class TextTemplateDto extends DocumentTemplateDto {

    @NotEmpty(message = "{value.mandatory}")
    private String text;

    @JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
    public TextTemplateDto(
            @JsonProperty("name") String name,
            @JsonProperty("text") String text) {
        super(name);
        this.text = text;
    }
}
