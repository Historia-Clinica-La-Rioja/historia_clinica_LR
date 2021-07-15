package net.pladema.snowstorm.services.domain.nomivac;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import net.pladema.snowstorm.services.domain.SnowstormItemResponse;

@Getter
@Setter
@ToString
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SnowstormNomivacItemResponse {

    private Boolean active;
    private Boolean released;
    private Long releasedEffectiveTime;
    private String memberId;
    private String moduleId;
    private String refsetId;
    private String referencedComponentId;
    private SnowstormNomivacAdditionalFields additionalFields;
    private SnowstormItemResponse referencedComponent;
    private String effectiveTime;

    @JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
    public SnowstormNomivacItemResponse(@JsonProperty("active") Boolean active,
                                        @JsonProperty("released") Boolean released,
                                        @JsonProperty("releasedEffectiveTime") Long releasedEffectiveTime,
                                        @JsonProperty("memberId") String memberId,
                                        @JsonProperty("moduleId") String moduleId,
                                        @JsonProperty("refsetId") String refsetId,
                                        @JsonProperty("referencedComponentId") String referencedComponentId,
                                        @JsonProperty("additionalFields") SnowstormNomivacAdditionalFields additionalFields,
                                        @JsonProperty("referencedComponent") SnowstormItemResponse referencedComponent,
                                        @JsonProperty("effectiveTime") String effectiveTime){
        this.active = active;
        this.released = released;
        this.releasedEffectiveTime = releasedEffectiveTime;
        this.memberId = memberId;
        this.moduleId = moduleId;
        this.refsetId = refsetId;
        this.referencedComponentId = referencedComponentId;
        this.additionalFields = additionalFields;
        this.referencedComponent = referencedComponent;
        this.effectiveTime = effectiveTime;
    }

    public String getMapTarget() {
        return this.additionalFields.getMapTarget();
    }
}
