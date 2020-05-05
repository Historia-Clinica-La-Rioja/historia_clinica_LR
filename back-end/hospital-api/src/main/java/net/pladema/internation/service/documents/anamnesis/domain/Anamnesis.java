package net.pladema.internation.service.documents.anamnesis.domain;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import net.pladema.internation.repository.masterdata.entity.DocumentStatus;
import net.pladema.internation.service.ips.domain.*;

import java.util.List;

@Getter
@Setter
@ToString
public class Anamnesis {

    private Long id;

    private boolean confirmed;

    private DocumentObservations notes;

    private List<DiagnosisBo> diagnosis;

    private List<HealthHistoryConditionBo> personalHistories;

    private List<HealthHistoryConditionBo> familyHistories;

    private List<MedicationBo> medications;

    private List<InmunizationBo> inmunizations;

    private List<AllergyConditionBo> allergies;

    private AnthropometricDataBo anthropometricData;

    private VitalSignBo vitalSigns;

    public String getDocumentStatusId(){
        return confirmed ? DocumentStatus.FINAL : DocumentStatus.DRAFT;
    }
}
