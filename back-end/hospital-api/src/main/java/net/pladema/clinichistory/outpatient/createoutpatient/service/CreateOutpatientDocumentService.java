package net.pladema.clinichistory.outpatient.createoutpatient.service;

import net.pladema.clinichistory.outpatient.createoutpatient.service.domain.OutpatientDocumentBo;

public interface CreateOutpatientDocumentService {
    
    OutpatientDocumentBo create(Integer outpatientId, Integer patientId, OutpatientDocumentBo outpatient);
}
