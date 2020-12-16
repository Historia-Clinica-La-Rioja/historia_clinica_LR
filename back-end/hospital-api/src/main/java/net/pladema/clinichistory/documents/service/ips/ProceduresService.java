package net.pladema.clinichistory.documents.service.ips;

import net.pladema.clinichistory.documents.service.domain.PatientInfoBo;
import net.pladema.clinichistory.outpatient.createoutpatient.service.domain.ProcedureBo;

import java.util.List;

public interface ProceduresService {

    List<ProcedureBo> loadProcedures(PatientInfoBo patientInfo, Long documentId, List<ProcedureBo> procedures);

}
