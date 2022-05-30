package net.pladema.booking.repository;

import net.pladema.booking.repository.entity.BackofficeMandatoryMedicalPractice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BackofficeMandatoryMedicalPracticeRepository extends JpaRepository<BackofficeMandatoryMedicalPractice, Integer> {
}
