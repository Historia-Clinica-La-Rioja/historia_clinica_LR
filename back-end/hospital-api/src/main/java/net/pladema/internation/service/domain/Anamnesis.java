package net.pladema.internation.service.domain;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import net.pladema.internation.controller.dto.ips.*;
import net.pladema.internation.service.domain.ips.*;

import java.io.Serializable;
import java.util.List;

@Getter
@Setter
@ToString
public class Anamnesis implements Serializable {

    private Integer anamnesisId;

    private String documentStatusId;

    private Observations notes;

    private List<HealthConditionBo> diagnosis;

    private List<HealthHistoryCondition> personalHistory;

    private List<HealthHistoryCondition> familyHistory;

    private List<Medication> medication;

    private List<Inmunization> inmunization;

    private List<AllergyCondition> allergy;

    private AnthropometricData anthropometricData;

    private VitalSignDto vitalSigns;

}
