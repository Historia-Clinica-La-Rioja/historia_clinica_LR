package net.pladema.loinc.infrastructure.output.repository;

import net.pladema.loinc.infrastructure.output.entity.LoincCode;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LoincCodeRepository extends JpaRepository<LoincCode, Integer> {
}
