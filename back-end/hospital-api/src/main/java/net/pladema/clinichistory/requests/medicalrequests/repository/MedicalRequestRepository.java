package net.pladema.clinichistory.requests.medicalrequests.repository;

import net.pladema.clinichistory.requests.medicalrequests.repository.entity.MedicalRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MedicalRequestRepository extends JpaRepository<MedicalRequest, Integer> {

}
