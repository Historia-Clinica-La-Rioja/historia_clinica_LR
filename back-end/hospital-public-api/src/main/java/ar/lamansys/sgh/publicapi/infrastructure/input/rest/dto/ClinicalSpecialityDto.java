package ar.lamansys.sgh.publicapi.infrastructure.input.rest.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

@Getter
@Setter
@ToString
@Builder
public class ClinicalSpecialityDto implements Serializable {

    private SnomedDto snomed;
}
