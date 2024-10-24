package commercial_medication.update_schema.cache.infrastructure.repository;

import commercial_medication.update_schema.cache.infrastructure.repository.entity.CommercialMedicationArticleCoverage;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CommercialMedicationArticleCoverageRepository extends JpaRepository<CommercialMedicationArticleCoverage, Integer> {
}
