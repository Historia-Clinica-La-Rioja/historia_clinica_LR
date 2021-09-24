package ar.lamansys.sgh.publicapi.infrastructure.input.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

@Getter
@Setter
@ToString
public class SupplyInformationDto implements Serializable {

    SnomedDto snomed;
}
