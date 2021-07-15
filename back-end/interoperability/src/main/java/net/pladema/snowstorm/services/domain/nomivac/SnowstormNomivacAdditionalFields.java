package net.pladema.snowstorm.services.domain.nomivac;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SnowstormNomivacAdditionalFields {

    private String mapTarget;

    @JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
    public SnowstormNomivacAdditionalFields(@JsonProperty("mapTarget") String mapTarget) {
        this.mapTarget = mapTarget;
    }
}
