package net.pladema.clinichistory.requests.servicerequests.service;

import ar.lamansys.sgh.clinichistory.domain.ips.DiagnosticReportBo;
import net.pladema.clinichistory.requests.servicerequests.service.domain.DiagnosticReportFilterBo;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface ListDiagnosticReportInfoService {
    List<DiagnosticReportBo> execute(DiagnosticReportFilterBo diagnosticReportFilterBo);
}
