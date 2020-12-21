package net.pladema.clinichistory.requests.servicerequests.service;

import net.pladema.clinichistory.requests.servicerequests.service.domain.DiagnosticReportBo;
import net.pladema.clinichistory.requests.servicerequests.service.domain.DiagnosticReportFilterBo;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface ListDiagnosticReportInfoService {
    List<DiagnosticReportBo> execute(DiagnosticReportFilterBo diagnosticReportFilterBo);
}
