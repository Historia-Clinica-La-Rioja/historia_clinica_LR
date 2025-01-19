package net.pladema.clinichistory.requests.servicerequests.repository;

import java.util.List;

public interface ListStudyOrderReportRepository {
    List<Object[]> execute(Integer patientId);
}
