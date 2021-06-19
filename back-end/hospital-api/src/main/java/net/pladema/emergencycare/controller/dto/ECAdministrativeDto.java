package net.pladema.emergencycare.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.Value;
import ar.lamansys.sgh.clinichistory.infrastructure.input.rest.ips.dto.SnomedDto;
import net.pladema.emergencycare.triage.controller.dto.TriageAdministrativeDto;

import javax.validation.Valid;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Value
@Builder
@ToString
@AllArgsConstructor
@NoArgsConstructor(force = true)
public class ECAdministrativeDto implements Serializable {

    @Valid
    NewEmergencyCareDto administrative;

    TriageAdministrativeDto triage;

    public List<SnomedDto> reasons() {
        return (this.getAdministrative() != null && this.getAdministrative().getReasons() != null) ?
                this.getAdministrative().getReasons() : new ArrayList<>();
    }

}
