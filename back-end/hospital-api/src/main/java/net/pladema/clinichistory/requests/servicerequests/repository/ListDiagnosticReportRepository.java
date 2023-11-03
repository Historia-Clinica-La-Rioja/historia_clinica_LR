package net.pladema.clinichistory.requests.servicerequests.repository;


import net.pladema.clinichistory.requests.servicerequests.repository.domain.DiagnosticReportFilterVo;

import java.util.List;

public interface ListDiagnosticReportRepository {
    List<Object[]> getList(DiagnosticReportFilterVo filter);

	List<Object[]> getMedicalOrderList(DiagnosticReportFilterVo filter);
}
