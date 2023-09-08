package net.pladema.template.infrastructure.input.rest.dto;

import ar.lamansys.sgh.clinichistory.infrastructure.input.rest.ips.dto.ConclusionDto;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class ConclusionTemplateDto extends DocumentTemplateDto {

    @NotEmpty(message = "{value.mandatory}")
    private String templateText;

    @NotNull(message = "{value.mandatory}")
    private List<ConclusionDto> conclusions;

    @JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
    public ConclusionTemplateDto(
            @JsonProperty("name") String name,
            @JsonProperty("templateText") String templateText,
            @JsonProperty("conclusions") List<ConclusionDto> conclusions) {

        super(name);
        this.templateText = templateText;
        this.conclusions = conclusions;
    }
}
