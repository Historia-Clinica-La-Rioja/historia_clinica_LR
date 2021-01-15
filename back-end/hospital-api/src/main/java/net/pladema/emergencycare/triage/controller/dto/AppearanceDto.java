package net.pladema.emergencycare.triage.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Getter
@AllArgsConstructor
@NoArgsConstructor(force = true)
@Builder
public class AppearanceDto  implements Serializable {
    
    private final Short bodyTemperatureId;
    
    private final Boolean cryingExcessive;
    
    private final Short muscleHypertonia;
    
}
