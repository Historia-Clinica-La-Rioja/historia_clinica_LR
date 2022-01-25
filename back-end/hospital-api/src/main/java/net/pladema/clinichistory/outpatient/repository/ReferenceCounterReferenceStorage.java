package net.pladema.clinichistory.outpatient.repository;

import net.pladema.clinichistory.outpatient.createoutpatient.service.domain.reference.CounterReferenceSummaryBo;
import net.pladema.clinichistory.outpatient.createoutpatient.service.domain.reference.ReferenceCounterReferenceFileBo;

import java.util.List;

public interface ReferenceCounterReferenceStorage {

    CounterReferenceSummaryBo getCounterReference(Integer referenceId);

    List<ReferenceCounterReferenceFileBo> getReferenceFilesData(Integer referenceId);

}
