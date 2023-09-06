package ar.lamansys.sgh.clinichistory.infrastructure.input.rest.ips.dto;

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
public class ConclusionDto extends HealthConditionDto {

    @JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
    public ConclusionDto(
            @JsonProperty("snomed") SnomedJsonDto snomed) {
        this.setSnomed(snomed);
    }
}
