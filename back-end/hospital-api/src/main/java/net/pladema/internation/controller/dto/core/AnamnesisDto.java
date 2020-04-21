package net.pladema.internation.controller.dto.core;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import net.pladema.internation.controller.dto.ObservationsDto;
import net.pladema.internation.controller.dto.ips.*;

import java.io.Serializable;
import java.util.List;

@Getter
@Setter
@ToString
public class AnamnesisDto implements Serializable {

    private String documentStatusId;

    private ObservationsDto notes;

    private List<HealthConditionDto> diagnosis;

    private List<HealthHistoryConditionDto> personalHistory;

    private List<HealthHistoryConditionDto> familyHistory;

    private List<MedicationDto> medication;

    private List<InmunizationDto> inmunization;

    private List<AllergyConditionDto> allergy;

    private AnthropometricDataDto anthropometricData;

    private VitalSignDto vitalSigns;

}
