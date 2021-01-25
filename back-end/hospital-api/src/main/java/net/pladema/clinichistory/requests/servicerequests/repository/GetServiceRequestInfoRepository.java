package net.pladema.clinichistory.requests.servicerequests.repository;

import net.pladema.clinichistory.requests.servicerequests.service.domain.DiagnosticReportBo;

import java.util.List;

public interface GetServiceRequestInfoRepository {
    List<Object[]> run(Integer serviceRequestId);

}
