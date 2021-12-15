package ar.lamansys.sgh.publicapi.infrastructure.output;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ExternalEncounterRepository extends JpaRepository<ExternalEncounter, Integer> {

    @Query(value = "SELECT ec FROM ExternalEncounter ec WHERE ec.externalEncounterId = :externalEncounterId ")
    Optional<ExternalEncounter> getByExternalEncounterId(@Param("externalEncounterId") String externalEncounterId);

}
