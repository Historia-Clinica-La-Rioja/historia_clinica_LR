package net.pladema.clinichistory.documents.service.ips;

import net.pladema.clinichistory.documents.service.domain.PatientInfoBo;
import ar.lamansys.sgh.clinichistory.domain.ips.ProcedureBo;

import java.util.List;

public interface ProceduresService {

    List<ProcedureBo> loadProcedures(PatientInfoBo patientInfo, Long documentId, List<ProcedureBo> procedures);

}
