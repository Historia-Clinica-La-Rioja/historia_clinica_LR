package net.pladema.clinichistory.requests.servicerequests.repository;

import ar.lamansys.sgx.shared.auditable.repository.SGXAuditableEntityJPARepository;
import net.pladema.clinichistory.requests.servicerequests.repository.entity.ServiceRequest;

import org.springframework.stereotype.Repository;

@Repository
public interface ServiceRequestRepository extends SGXAuditableEntityJPARepository<ServiceRequest, Integer> {

}
