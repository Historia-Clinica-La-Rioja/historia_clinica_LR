package ar.lamansys.sgh.publicapi.infrastructure.input.dto;

import ar.lamansys.sgx.shared.dates.controller.dto.DateDto;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

@Getter
@Setter
@ToString
public class InternmentDto implements Serializable {

    String id;
    DateDto entryDate;
    DateDto dischargeDate;
}
