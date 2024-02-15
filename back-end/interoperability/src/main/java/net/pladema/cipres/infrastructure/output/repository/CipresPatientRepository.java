package net.pladema.cipres.infrastructure.output.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CipresPatientRepository extends JpaRepository<CipresPatient, Integer> {

	@Query(value = "SELECT cp.pk.cipresPatientId FROM CipresPatient cp WHERE cp.pk.patientId = :patientId ")
	Optional<Long> getCipresPatientId(@Param("patientId") Integer patientId);

}
