package net.pladema.snowstorm.services.domain;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.node.ArrayNode;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SnowstormSearchResponse {

    private List<SnowstormItemResponse> items;
    private Integer limit;
    private Integer total;
    private Integer offset;
    private String searchAfter;
    private ArrayNode searchAfterArray;

    @JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
    public SnowstormSearchResponse (@JsonProperty("items") List<SnowstormItemResponse> items,
                                    @JsonProperty("limit") Integer limit,
                                    @JsonProperty("total") Integer total,
                                    @JsonProperty("offset") Integer offset,
                                    @JsonProperty("searchAfter") String searchAfter,
                                    @JsonProperty("searchAfterArray") ArrayNode searchAfterArray){
        this.offset = offset;
        this.searchAfter = searchAfter;
        this.items = items;
        this.limit = limit;
        this.total = total;
        this.searchAfterArray = searchAfterArray;
    }

}
