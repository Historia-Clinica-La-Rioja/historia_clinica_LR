package net.pladema.snowstorm.services.domain;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SnowstormConcept {

    private List<SnowstormItemResponse> items;

    @JsonCreator
    public SnowstormConcept(List<SnowstormItemResponse> items){
        this.items = items;
    }
}
