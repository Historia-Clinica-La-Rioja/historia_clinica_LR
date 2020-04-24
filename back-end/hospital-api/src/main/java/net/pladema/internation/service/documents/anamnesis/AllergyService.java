package net.pladema.internation.service.documents.anamnesis;

import net.pladema.internation.service.domain.ips.AllergyCondition;

import java.util.List;

public interface AllergyService {

    public void loadAllergies(Integer patientId, Long id, List<AllergyCondition> allergy);
}
