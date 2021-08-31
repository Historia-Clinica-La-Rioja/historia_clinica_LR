package net.pladema.snowstorm.services.domain.nomivac;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@ToString
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SnowstormNomivacRefsetMembersResponse {

    private List<SnowstormNomivacItemResponse> items;
    private Integer limit;
    private Integer total;
    private Integer offset;

    @JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
    public SnowstormNomivacRefsetMembersResponse(@JsonProperty("items") List<SnowstormNomivacItemResponse> items,
                                                 @JsonProperty("limit") Integer limit,
                                                 @JsonProperty("total") Integer total,
                                                 @JsonProperty("offset") Integer offset){
        this.offset = offset;
        this.items = items;
        this.limit = limit;
        this.total = total;
    }
}
