package net.pladema.clinichistory.requests.servicerequests.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import net.pladema.clinichistory.requests.servicerequests.repository.entity.TranscribedServiceRequest;

import org.springframework.transaction.annotation.Transactional;

@Repository
public interface TranscribedServiceRequestRepository extends JpaRepository<TranscribedServiceRequest, Integer> {

	@Transactional(readOnly = true)
	@Query("SELECT tsr.studyId " +
			"FROM TranscribedServiceRequest tsr " +
			"WHERE tsr.id = :orderId ")
	Integer getStudyIdByOrderId(@Param("orderId") Integer orderId);

}
