package ar.lamansys.sgh.publicapi.infrastructure.input.rest.dto;

import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@ToString
@Builder
public class SnomedDto implements Serializable {

    private String sctId;
    private String pt;
}
