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

    private List<HealthHistoryCondition> personalHistory;

    private List<HealthHistoryCondition> familyHistory;

    private List<Medication> medication;

    private List<Inmunization> inmunization;

    private List<AllergyCondition> allergy;

    private AnthropometricDataBo anthropometricData;

    private VitalSignBo vitalSigns;

}
