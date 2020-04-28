package net.pladema.internation.service.domain;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import net.pladema.internation.service.domain.ips.*;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@ToString
public class InternmentGeneralState implements Serializable {

    private List<HealthConditionBo> diagnosis;

    private List<HealthHistoryCondition> personalHistories;

    private List<HealthHistoryCondition> familyHistories;

    private List<Medication> medications;

    private List<InmunizationBo> inmunizations;

    private List<AllergyConditionBo> allergies;

    private List<AnthropometricDataBo> anthropometricData = new ArrayList<>();

    private List<VitalSignBo> vitalSigns = new ArrayList<>();

}
