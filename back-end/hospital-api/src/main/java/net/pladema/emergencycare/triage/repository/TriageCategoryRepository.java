package net.pladema.emergencycare.triage.repository;

import net.pladema.emergencycare.triage.repository.entity.TriageCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TriageCategoryRepository extends JpaRepository<TriageCategory, Short> {}