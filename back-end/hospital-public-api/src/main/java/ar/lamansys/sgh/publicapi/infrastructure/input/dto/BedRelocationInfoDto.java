package ar.lamansys.sgh.publicapi.infrastructure.input.dto;

import ar.lamansys.sgx.shared.dates.controller.dto.DateDto;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

@Getter
@Setter
@ToString
public class BedRelocationInfoDto implements Serializable {

    DateDto relocationDate;
    Integer careTypeId; // example: 1 (minimos), 2 (intermedio), 3(intensivos)
    ClinicalSpecialityDto service;
}
