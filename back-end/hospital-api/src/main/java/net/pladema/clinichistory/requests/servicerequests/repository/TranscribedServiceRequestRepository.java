package net.pladema.clinichistory.requests.servicerequests.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import net.pladema.clinichistory.requests.servicerequests.repository.entity.TranscribedServiceRequest;

@Repository
public interface TranscribedServiceRequestRepository extends JpaRepository<TranscribedServiceRequest, Integer> {

}
