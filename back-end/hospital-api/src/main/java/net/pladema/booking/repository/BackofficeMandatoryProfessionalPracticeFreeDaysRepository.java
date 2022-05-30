package net.pladema.booking.repository;

import net.pladema.booking.repository.entity.BackofficeMandatoryProfessionalPracticeFreeDays;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BackofficeMandatoryProfessionalPracticeFreeDaysRepository
        extends JpaRepository<BackofficeMandatoryProfessionalPracticeFreeDays, Integer> {
}
