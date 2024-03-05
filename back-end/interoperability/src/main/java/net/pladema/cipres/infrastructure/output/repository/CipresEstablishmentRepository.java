package net.pladema.cipres.infrastructure.output.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CipresEstablishmentRepository extends JpaRepository<CipresEstablishment, CipresEstablishmentPk> {

	@Query("SELECT ce.pk.cipresEstablishmentId FROM CipresEstablishment ce WHERE ce.pk.sisaCode = :sisaCode")
	Optional<String> findBySisaCode(@Param("sisaCode") String sisaCode);

}
