package net.pladema.emergencycare.triage.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.annotation.Nullable;
import java.io.Serializable;

@Getter
@AllArgsConstructor
@NoArgsConstructor(force = true)
@Builder
public class AppearanceDto  implements Serializable {

    @Nullable
    private final Short bodyTemperatureId;

    @Nullable
    private final Boolean cryingExcessive;

    @Nullable
    private final Short muscleHypertoniaId;
    
}
