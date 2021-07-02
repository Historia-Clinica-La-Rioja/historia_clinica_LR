package ar.lamansys.immunization.infrastructure.input.rest.dto;

import ar.lamansys.sgh.clinichistory.infrastructure.input.rest.ips.dto.ImmunizationDto;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.validation.annotation.Validated;

import java.util.List;

@Getter
@Setter
@ToString
@Validated
public class ImmunizePatientDto {

    private Integer clinicalSpecialtyId;

    private List<ImmunizationDto> immunizations;

}
