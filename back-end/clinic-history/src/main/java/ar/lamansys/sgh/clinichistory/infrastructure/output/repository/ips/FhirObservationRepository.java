package ar.lamansys.sgh.clinichistory.infrastructure.output.repository.ips;

import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.ips.entity.FhirObservation;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FhirObservationRepository extends JpaRepository<FhirObservation, Integer> {
	List<FhirObservation> findByFhirObservationGroupId(Integer id);
}
