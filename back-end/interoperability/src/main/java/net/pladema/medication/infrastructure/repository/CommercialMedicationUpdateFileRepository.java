package net.pladema.medication.infrastructure.repository;

import net.pladema.medication.infrastructure.repository.entity.CommercialMedicationUpdateFile;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CommercialMedicationUpdateFileRepository extends JpaRepository<CommercialMedicationUpdateFile, Long> {
}
