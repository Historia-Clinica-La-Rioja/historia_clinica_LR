package net.pladema.clinichistory.outpatient.createoutpatient.service;

import net.pladema.clinichistory.outpatient.createoutpatient.service.domain.OutpatientBo;

public interface CreateOutpatientConsultationService {

    OutpatientBo create(Integer institutionId, Integer patientId, Integer doctorId, boolean billable,
                        Integer clinicalSpecialtyId, Integer patientMedicalCoverageId);
}
