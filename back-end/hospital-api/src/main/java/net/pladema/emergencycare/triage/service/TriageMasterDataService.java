package net.pladema.emergencycare.triage.service;

import net.pladema.emergencycare.triage.service.domain.TriageCategoryBo;

import java.util.List;

public interface TriageMasterDataService {

    List<TriageCategoryBo> getCategories();

}
