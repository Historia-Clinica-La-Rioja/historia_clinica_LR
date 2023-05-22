package net.pladema.cipres.infrastructure.output.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CipresEncounterRepository extends JpaRepository<CipresEncounter, Integer> {
}