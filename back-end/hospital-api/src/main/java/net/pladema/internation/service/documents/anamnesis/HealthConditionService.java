package net.pladema.internation.service.documents.anamnesis;

import net.pladema.internation.service.domain.ips.HealthConditionBo;
import net.pladema.internation.service.domain.ips.HealthHistoryCondition;

import java.util.List;

public interface HealthConditionService {

    public void loadDiagnosis(Integer patientId, Long documentId, List<HealthConditionBo> diagnosis);

    public void loadPersonalHistories(Integer patientId, Long documentId, List<HealthHistoryCondition> personalHistories);

    public void loadFamilyHistories(Integer patientId, Long documentId, List<HealthHistoryCondition> familyHistories);
}
