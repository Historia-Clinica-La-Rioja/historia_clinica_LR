package net.pladema.loinc.infrastructure.output.repository;

import net.pladema.loinc.infrastructure.output.entity.LoincSystem;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LoincSystemRepository extends JpaRepository<LoincSystem, Short> {
}
