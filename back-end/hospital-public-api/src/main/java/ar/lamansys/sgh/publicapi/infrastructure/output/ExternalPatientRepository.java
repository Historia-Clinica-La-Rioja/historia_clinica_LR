package ar.lamansys.sgh.publicapi.infrastructure.output;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ExternalPatientRepository extends JpaRepository<ExternalPatient, ExternalPatientPK> {

    @Query(value = "SELECT ep FROM ExternalPatient ep WHERE ep.externalPatientPK.externalId = :externalId ")
    Optional<ExternalPatient> findByExternalId(@Param("externalId") String externalId);

    @Query(value = "SELECT ep FROM ExternalPatient ep WHERE ep.externalPatientPK.patientId = :patientId ")
    Optional<ExternalPatient> findByPatientId(@Param("patientId") Integer patientId);
}
