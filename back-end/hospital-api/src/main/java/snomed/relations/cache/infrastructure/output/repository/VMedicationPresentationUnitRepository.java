package snomed.relations.cache.infrastructure.output.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.stereotype.Repository;

import snomed.relations.cache.infrastructure.output.repository.entity.VMedicationPresentationUnit;

@Repository
public interface VMedicationPresentationUnitRepository extends JpaRepository<VMedicationPresentationUnit, String> {
}
