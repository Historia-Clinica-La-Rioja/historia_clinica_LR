package ar.lamansys.sgh.clinichistory.infrastructure.input.rest.ips.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class SnomedJsonDto extends SnomedDto {

    @JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
    public SnomedJsonDto(
            @JsonProperty("sctid") String sctid,
            @JsonProperty("pt") String pt) {
        super(sctid, pt);
    }
}
