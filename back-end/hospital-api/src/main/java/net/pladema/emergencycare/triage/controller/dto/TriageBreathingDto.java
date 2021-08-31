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
public class TriageBreathingDto implements Serializable {

    private MasterDataDto respiratoryRetraction;

    private Boolean stridor;

    private NewEffectiveClinicalObservationDto respiratoryRate;

    private NewEffectiveClinicalObservationDto bloodOxygenSaturation;

    public boolean hasNoValues() {
        return Stream.of(respiratoryRetraction, stridor, respiratoryRate, bloodOxygenSaturation).allMatch(Objects::isNull);
    }

}
