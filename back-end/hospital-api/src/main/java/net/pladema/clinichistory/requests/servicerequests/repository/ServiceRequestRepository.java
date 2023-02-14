package net.pladema.clinichistory.requests.servicerequests.repository;

import ar.lamansys.sgx.shared.auditable.repository.SGXAuditableEntityJPARepository;
import net.pladema.clinichistory.requests.servicerequests.repository.entity.ServiceRequest;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface ServiceRequestRepository extends SGXAuditableEntityJPARepository<ServiceRequest, Integer> {

	@Transactional(readOnly = true)
	@Query("SELECT sr.id " +
			"FROM ServiceRequest sr " +
			"WHERE sr.patientId IN :patientsIds")
	List<Integer> getServiceRequestIdsFromPatients(@Param("patientsIds") List<Integer> patientsIds);

	@Transactional
	@Query("SELECT sr.id " +
			"FROM ServiceRequest sr " +
			"WHERE sr.sourceId IN :sourceIds " +
			"AND sr.sourceTypeId = :typeId")
	List<Integer> getServiceRequestIdsFromIdSourceType(@Param("sourceIds") List<Integer> sourceIds, @Param("typeId") Short typeId);
}
