package net.pladema.internation.controller.internment.dto.internmentstate;

import lombok.*;
import net.pladema.internation.controller.ips.dto.DiagnosisDto;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class DiagnosesGeneralStateDto extends DiagnosisDto {

   private boolean main;

}
