package net.pladema.clinichistory.hospitalization.controller.dto.internmentstate;

import lombok.*;
import net.pladema.clinichistory.hospitalization.controller.generalstate.dto.DiagnosisDto;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class DiagnosesGeneralStateDto extends DiagnosisDto {

   private boolean main;

}
