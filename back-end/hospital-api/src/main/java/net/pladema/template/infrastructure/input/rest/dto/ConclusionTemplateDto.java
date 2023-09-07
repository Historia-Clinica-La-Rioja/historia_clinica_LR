package net.pladema.template.infrastructure.input.rest.dto;

import ar.lamansys.sgh.clinichistory.infrastructure.input.rest.ips.dto.ConclusionDto;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class ConclusionTemplateDto extends DocumentTemplateDto {

    private List<ConclusionDto> conclusions;

    @JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
    public ConclusionTemplateDto(
            @JsonProperty("name") String name,
            @JsonProperty("category") String category,
            @JsonProperty("templateText") String templateText,
            @JsonProperty("conclusions") List<ConclusionDto> conclusions) {

        super(name, category, templateText);
        this.conclusions = conclusions;
    }
}
