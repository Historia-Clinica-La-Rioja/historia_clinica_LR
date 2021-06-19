package ar.lamansys.sgh.clinichistory.infrastructure.input.rest.ips.dto;

import lombok.*;
import ar.lamansys.sgh.clinichistory.infrastructure.input.rest.ips.dto.DiagnosisDto;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class DiagnosesGeneralStateDto extends DiagnosisDto {

   private boolean main;

}
