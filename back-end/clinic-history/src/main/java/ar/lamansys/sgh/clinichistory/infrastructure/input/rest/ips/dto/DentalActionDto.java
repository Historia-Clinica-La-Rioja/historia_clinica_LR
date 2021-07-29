package ar.lamansys.sgh.clinichistory.infrastructure.input.rest.ips.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class DentalActionDto extends ClinicalTermDto {

    private SnomedDto tooth;

    private SnomedDto surface;

    private boolean diagnostic;

}
