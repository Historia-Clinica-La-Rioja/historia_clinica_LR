package net.pladema.emergencycare.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.Value;
import net.pladema.clinichistory.documents.controller.dto.NewVitalSignsObservationDto;
import net.pladema.clinichistory.hospitalization.controller.generalstate.dto.SnomedDto;
import net.pladema.emergencycare.triage.controller.dto.TriageAdultGynecologicalDto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Value
@Builder
@ToString
@AllArgsConstructor
@NoArgsConstructor(force = true)
public class ECAdultGynecologicalDto implements Serializable {

    NewEmergencyCareDto administrative;

    TriageAdultGynecologicalDto triage;

    public Integer patientId() {
        return (this.getAdministrative() != null
                && this.getAdministrative().getPatient() != null
                && this.getAdministrative().getPatient().getId() != null) ?
                this.getAdministrative().getPatient().getId() : null;
    }

    public NewVitalSignsObservationDto vitalSignsObservation() {
        return (this.triage != null && this.getTriage().getVitalSigns() != null) ?
                this.getTriage().getVitalSigns() : new NewVitalSignsObservationDto();
    }

    public List<SnomedDto> reasons() {
        return (this.getAdministrative() != null && this.getAdministrative().getReasons() != null) ?
                this.getAdministrative().getReasons() : new ArrayList<>();
    }

}
