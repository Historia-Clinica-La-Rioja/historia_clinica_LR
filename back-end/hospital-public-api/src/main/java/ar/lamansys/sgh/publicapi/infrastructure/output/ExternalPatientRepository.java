package ar.lamansys.sgh.publicapi.infrastructure.output;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ExternalPatientRepository extends JpaRepository<ExternalPatient, Integer> {

    @Query(value = "SELECT * FROM external_patient ep WHERE ep.external_id = :externalId ORDER BY ep.id DESC LIMIT 1",nativeQuery = true)
    Optional<ExternalPatient> findByExternalId(@Param("externalId") String externalId);
}
