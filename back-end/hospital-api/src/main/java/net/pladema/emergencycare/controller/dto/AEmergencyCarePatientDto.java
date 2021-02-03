package net.pladema.emergencycare.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.annotation.Nullable;
import java.io.Serializable;

@Builder
@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor(force = true)
public class AEmergencyCarePatientDto implements Serializable {

    @Nullable
    private final Integer id;

    @Nullable
    private final Integer patientMedicalCoverageId;

}
