package net.pladema.snowstorm.services.domain;

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
public class SnowstormCie10AdditionalFields {

    private String mapCategoryId;
    private String mapRule;
    private String mapAdvice;
    private String mapPriority;
    private String mapGroup;
    private String correlationId;
    private String mapTarget;

    @JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
    public SnowstormCie10AdditionalFields(@JsonProperty("mapCategoryId") String mapCategoryId,
                                          @JsonProperty("mapRule") String mapRule,
                                          @JsonProperty("mapAdvice") String mapAdvice,
                                          @JsonProperty("mapPriority") String mapPriority,
                                          @JsonProperty("mapGroup") String mapGroup,
                                          @JsonProperty("correlationId") String correlationId,
                                          @JsonProperty("mapTarget") String mapTarget) {
        this.mapCategoryId = mapCategoryId;
        this.mapRule = mapRule;
        this.mapAdvice = mapAdvice;
        this.mapPriority = mapPriority;
        this.mapGroup = mapGroup;
        this.correlationId = correlationId;
        this.mapTarget = mapTarget;
    }
}
