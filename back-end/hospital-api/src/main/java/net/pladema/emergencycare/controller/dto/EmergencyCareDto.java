package net.pladema.emergencycare.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.ToString;
import lombok.Value;
import net.pladema.emergencycare.triage.controller.dto.TriageDto;
import net.pladema.emergencycare.controller.dto.administrative.NewECAdministrativeDto;

import java.io.Serializable;

@Value
@Builder
@ToString
@AllArgsConstructor
public class EmergencyCareDto implements Serializable {

    NewECAdministrativeDto administrative;

    TriageDto triage;
}
