package net.pladema.internation.service.internment.maindiagnoses.domain;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import net.pladema.internation.service.documents.InternmentDocument;
import net.pladema.internation.service.ips.domain.*;

import javax.annotation.Nullable;
import javax.validation.constraints.NotNull;
import java.util.Collections;
import java.util.List;

@Getter
@Setter
@ToString
public class MainDiagnosisBo implements InternmentDocument {

    private Long id;

    @NotNull
    private boolean confirmed = false;

    @Nullable
    private DocumentObservationsBo notes;

    @Nullable
    private HealthConditionBo mainDiagnosis;

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public List<DiagnosisBo> getDiagnosis() {
        return null;
    }

    @Override
    public List<HealthHistoryConditionBo> getPersonalHistories() {
        return Collections.emptyList();
    }

    @Override
    public List<HealthHistoryConditionBo> getFamilyHistories() {
        return Collections.emptyList();
    }

    @Override
    public List<MedicationBo> getMedications() {
        return Collections.emptyList();
    }

    @Override
    public List<AllergyConditionBo> getAllergies() {
        return Collections.emptyList();
    }

    @Override
    public List<InmunizationBo> getInmunizations() {
        return Collections.emptyList();
    }

    @Override
    public VitalSignBo getVitalSigns() {
        return null;
    }

    @Override
    public AnthropometricDataBo getAnthropometricData() {
        return null;
    }
}
