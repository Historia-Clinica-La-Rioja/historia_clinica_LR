package ar.lamansys.sgh.publicapi.infrastructure.input.dto;

import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@ToString
public class SnomedDto implements Serializable {

    Integer id;
    String sctId;
    String pt;
}
