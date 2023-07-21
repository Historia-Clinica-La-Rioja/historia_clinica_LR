package net.pladema.clinichistory.requests.servicerequests.service;

import java.util.List;

import ar.lamansys.sgh.clinichistory.domain.ips.TranscribedOrderReportInfoBo;

import org.springframework.stereotype.Service;

import ar.lamansys.sgh.clinichistory.domain.ips.TranscribedDiagnosticReportBo;

@Service
public interface ListTranscribedDiagnosticReportInfoService {
    List<TranscribedDiagnosticReportBo> execute(Integer patientId);

	List<TranscribedOrderReportInfoBo> getListTranscribedOrder(Integer patientId) ;
}
