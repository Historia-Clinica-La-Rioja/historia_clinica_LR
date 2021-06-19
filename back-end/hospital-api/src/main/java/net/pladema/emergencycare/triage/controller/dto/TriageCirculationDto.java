package net.pladema.emergencycare.triage.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import ar.lamansys.sgh.clinichistory.infrastructure.input.rest.ips.dto.NewEffectiveClinicalObservationDto;
import ar.lamansys.sgx.shared.masterdata.infrastructure.input.rest.dto.MasterDataDto;

import java.io.Serializable;
import java.util.Objects;
import java.util.stream.Stream;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class TriageCirculationDto implements Serializable {

    private MasterDataDto perfusion;

    private NewEffectiveClinicalObservationDto heartRate;

    public boolean hasNoValues() {
        return Stream.of(perfusion, heartRate).allMatch(Objects::isNull);
    }

}
