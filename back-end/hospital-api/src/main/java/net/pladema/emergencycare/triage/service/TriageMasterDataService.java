package net.pladema.emergencycare.triage.service;

import ar.lamansys.sgh.shared.infrastructure.input.service.EBodyTemperature;
import ar.lamansys.sgh.shared.infrastructure.input.service.EMuscleHypertonia;
import ar.lamansys.sgh.shared.infrastructure.input.service.EPerfusion;
import ar.lamansys.sgh.shared.infrastructure.input.service.ERespiratoryRetraction;
import net.pladema.emergencycare.triage.service.domain.TriageCategoryBo;

import java.util.List;

public interface TriageMasterDataService {

    List<TriageCategoryBo> getCategories();

    TriageCategoryBo getCategoryById(Short categoryId);

    List<EBodyTemperature> getBodyTemperature();

    List<EMuscleHypertonia> getMuscleHypertonia();

    List<ERespiratoryRetraction> getRespiratoryRetraction();

    List<EPerfusion> getPerfusion();
}
