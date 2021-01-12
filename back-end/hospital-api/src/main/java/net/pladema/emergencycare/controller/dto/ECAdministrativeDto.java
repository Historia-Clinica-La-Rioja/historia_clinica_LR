package net.pladema.emergencycare.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.Value;
import net.pladema.emergencycare.triage.controller.dto.TriageAdministrativeDto;

import java.io.Serializable;

@Value
@Builder
@ToString
@AllArgsConstructor
@NoArgsConstructor(force = true)
public class ECAdministrativeDto implements Serializable {

    NewEmergencyCareDto administrative;

    TriageAdministrativeDto triage;

}
