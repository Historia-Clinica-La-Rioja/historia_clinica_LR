package net.pladema.internation.service.domain;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import net.pladema.internation.service.domain.ips.*;

import java.util.List;

@Getter
@Setter
@ToString
public class Anamnesis {

    private Long id;

    private String documentStatusId;

    private DocumentObservations notes;

    private List<DiagnosisBo> diagnosis;

    private List<HealthHistoryConditionBo> personalHistories;

    private List<HealthHistoryConditionBo> familyHistories;

    private List<MedicationBo> medications;

    private List<InmunizationBo> inmunizations;

    private List<AllergyConditionBo> allergies;

    private AnthropometricDataBo anthropometricData;

    private VitalSignBo vitalSigns;

}
