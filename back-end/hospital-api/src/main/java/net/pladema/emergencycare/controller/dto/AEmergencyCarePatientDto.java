package net.pladema.emergencycare.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;

@Builder
@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor(force = true)
public class AEmergencyCarePatientDto implements Serializable {

    private final Integer id;

    private final Integer patientMedicalCoverageId;

}
