package net.pladema.snowstorm.services.domain;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SnowstormItemResponse {

    private String conceptId;
    private Boolean active;
    private String definitionStatus;
    private String moduleId;
    private String effectiveTime;
    private String id;
    private ObjectNode fsn;
    private ObjectNode pt;

    @JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
    public SnowstormItemResponse (@JsonProperty("conceptId") String conceptId,
                                  @JsonProperty("active") Boolean active,
                                  @JsonProperty("definitionStatus") String definitionStatus,
                                  @JsonProperty("moduleId") String moduleId,
                                  @JsonProperty("effectiveTime") String effectiveTime,
                                  @JsonProperty("id") String id,
                                  @JsonProperty("fsn") ObjectNode fsn,
                                  @JsonProperty("pt") ObjectNode pt){
        this.conceptId = conceptId;
        this.active = active;
        this.definitionStatus = definitionStatus;
        this.moduleId = moduleId;
        this.effectiveTime = effectiveTime;
        this.id = id;
        this.fsn = fsn;
        this.pt = pt;
    }

}
