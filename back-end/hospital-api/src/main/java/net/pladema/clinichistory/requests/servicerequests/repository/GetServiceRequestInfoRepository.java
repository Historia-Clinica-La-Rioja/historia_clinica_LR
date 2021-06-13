package net.pladema.clinichistory.requests.servicerequests.repository;

import java.util.List;

public interface GetServiceRequestInfoRepository {
    List<Object[]> run(Integer serviceRequestId);

}
