package ar.lamansys.sgh.publicapi.infrastructure.input.rest.dto;

import java.io.Serializable;

import ar.lamansys.sgx.shared.dates.controller.dto.DateTimeDto;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@Builder
public class InternmentDto implements Serializable {

    private String id;
    private DateTimeDto entryDate;
    private DateTimeDto dischargeDate;
}
