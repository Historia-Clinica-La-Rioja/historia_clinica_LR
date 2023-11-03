package net.pladema.clinichistory.requests.servicerequests.repository;


import java.util.List;

public interface ListStudyWithoutOrderReportRepository {
    List<Object[]> execute(Integer patientId);
}
