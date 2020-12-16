package net.pladema.clinichistory.outpatient.createoutpatient.service;

import net.pladema.clinichistory.documents.service.domain.PatientInfoBo;
import net.pladema.clinichistory.outpatient.createoutpatient.service.domain.OutpatientDocumentBo;

public interface CreateOutpatientDocumentService {
    
    OutpatientDocumentBo create(Integer outpatientId, PatientInfoBo patientInfo, OutpatientDocumentBo outpatient);
}
