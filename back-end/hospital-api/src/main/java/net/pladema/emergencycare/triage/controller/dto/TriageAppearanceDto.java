package net.pladema.emergencycare.triage.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import ar.lamansys.sgx.shared.masterdata.infrastructure.input.rest.dto.MasterDataDto;

import java.io.Serializable;
import java.util.Objects;
import java.util.stream.Stream;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class TriageAppearanceDto implements Serializable {

    private MasterDataDto bodyTemperature;

    private Boolean cryingExcessive;

    private MasterDataDto muscleHypertonia;

    public boolean hasNoValues() {
        return Stream.of(bodyTemperature, cryingExcessive, muscleHypertonia).allMatch(Objects::isNull);
    }

}
