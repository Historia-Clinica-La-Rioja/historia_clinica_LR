package net.pladema.booking.repository;

import net.pladema.booking.repository.entity.BackofficeHealthInsurancePractice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BackofficeHealthInsurancePracticeRepository extends JpaRepository<BackofficeHealthInsurancePractice, Integer> {
}
