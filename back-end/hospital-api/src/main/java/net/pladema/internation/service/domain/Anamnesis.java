package net.pladema.internation.service.domain;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import net.pladema.internation.service.domain.ips.*;

import java.io.Serializable;
import java.util.List;

@Getter
@Setter
@ToString
public class Anamnesis implements Serializable {

    private Long id;

    private String documentStatusId;

    private DocumentObservations notes;

    private List<HealthConditionBo> diagnosis;

    private List<HealthHistoryCondition> personalHistories;

    private List<HealthHistoryCondition> familyHistories;

    private List<Medication> medications;

    private List<InmunizationBo> inmunizations;

    private List<AllergyConditionBo> allergies;

    private AnthropometricDataBo anthropometricData;

    private VitalSignBo vitalSigns;

}
