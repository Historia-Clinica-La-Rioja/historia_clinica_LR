package ar.lamansys.sgh.clinichistory.application.ports;

import ar.lamansys.sgh.clinichistory.domain.hce.HCEReferenceProblemBo;
import ar.lamansys.sgh.clinichistory.domain.hce.summary.CounterReferenceSummaryBo;
import ar.lamansys.sgh.clinichistory.domain.hce.summary.ReferenceCounterReferenceFileBo;

import java.util.List;

public interface HCEReferenceCounterReferenceStorage {

    CounterReferenceSummaryBo getCounterReference(Integer referenceId);

    List<ReferenceCounterReferenceFileBo> getReferenceFilesData(Integer referenceId);

    List<HCEReferenceProblemBo> getProblemsWithReferences(Integer patientId);
}
