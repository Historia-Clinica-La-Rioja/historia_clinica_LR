package net.pladema.clinichistory.outpatient.createoutpatient.service;

import net.pladema.clinichistory.outpatient.createoutpatient.service.domain.OutpatientEvolutionSummaryBo;
import net.pladema.clinichistory.outpatient.createoutpatient.service.domain.ConsultationsBo;

import java.util.List;

public interface OutpatientSummaryService {

    List<OutpatientEvolutionSummaryBo> getSummary(Integer patientId);

    List<ConsultationsBo> getOutpatientConsultations(Integer patientId);
}
