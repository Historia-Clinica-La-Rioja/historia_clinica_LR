package net.pladema.loinc.infrastructure.output.repository;

import net.pladema.loinc.infrastructure.output.entity.LoincStatus;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LoincStatusRepository extends JpaRepository<LoincStatus, Short> {
}
