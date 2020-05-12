package net.pladema.internation.service.documents.evolutionnote.domain;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import net.pladema.internation.repository.masterdata.entity.DocumentStatus;
import net.pladema.internation.service.ips.domain.*;

import java.util.List;

@Getter
@Setter
@ToString
public class EvolutionNote {

    private Long id;

    private boolean confirmed;

    private DocumentObservations notes;

    private HealthConditionBo mainDiagnosis;

    private List<DiagnosisBo> diagnosis;

    private List<InmunizationBo> inmunizations;

    private List<AllergyConditionBo> allergies;

    private AnthropometricDataBo anthropometricData;

    private VitalSignBo vitalSigns;

    public String getDocumentStatusId(){
        return confirmed ? DocumentStatus.FINAL : DocumentStatus.DRAFT;
    }
}
