package net.pladema.clinichistory.requests.servicerequests.service.impl;

import net.pladema.clinichistory.requests.servicerequests.repository.entity.DiagnosticReportFilterVo;
import net.pladema.clinichistory.requests.servicerequests.service.ListDiagnosticReportInfoService;
import net.pladema.clinichistory.requests.servicerequests.service.domain.DiagnosticReportBo;
import net.pladema.clinichistory.requests.servicerequests.service.domain.DiagnosticReportFilterBo;

import java.util.List;

public class ListDiagnosticReportInfoServiceImpl implements ListDiagnosticReportInfoService {
    @Override
    public List<DiagnosticReportBo> execute(DiagnosticReportFilterBo filter) {
        DiagnosticReportFilterVo filterVo = new DiagnosticReportFilterVo(
                filter.getPatientId(),
                filter.getStatus(),
                filter.getServiceRequest(),
                filter.getHealthCondition()
        );


        return null;
    }
}
