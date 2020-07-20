package net.pladema.clinichistory.ips.service;

import net.pladema.clinichistory.outpatient.createoutpatient.service.domain.ProcedureBo;

import java.util.List;

public interface ProceduresService {

    List<ProcedureBo> loadProcedures(Integer patientId, Long documentId, List<ProcedureBo> procedures);

}
