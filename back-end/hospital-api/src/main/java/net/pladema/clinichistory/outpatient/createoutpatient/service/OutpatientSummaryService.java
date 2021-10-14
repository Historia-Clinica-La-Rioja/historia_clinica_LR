package net.pladema.clinichistory.outpatient.createoutpatient.service;

import net.pladema.clinichistory.outpatient.createoutpatient.service.domain.EvolutionSummaryBo;

import java.util.List;

public interface OutpatientSummaryService {

    List<EvolutionSummaryBo> getSummary(Integer patientId);

}
