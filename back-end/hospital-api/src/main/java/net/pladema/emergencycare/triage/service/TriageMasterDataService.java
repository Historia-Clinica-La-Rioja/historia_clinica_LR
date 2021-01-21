package net.pladema.emergencycare.triage.service;

import net.pladema.emergencycare.triage.service.domain.TriageCategoryBo;
import net.pladema.emergencycare.triage.service.domain.enums.EBodyTemperature;
import net.pladema.emergencycare.triage.service.domain.enums.EMuscleHypertonia;
import net.pladema.emergencycare.triage.service.domain.enums.EPerfusion;
import net.pladema.emergencycare.triage.service.domain.enums.ERespiratoryRetraction;

import java.util.List;

public interface TriageMasterDataService {

    List<TriageCategoryBo> getCategories();

    TriageCategoryBo getCategoryById(Short categoryId);

    List<EBodyTemperature> getBodyTemperature();

    List<EMuscleHypertonia> getMuscleHypertonia();

    List<ERespiratoryRetraction> getRespiratoryRetraction();

    List<EPerfusion> getPerfusion();
}
