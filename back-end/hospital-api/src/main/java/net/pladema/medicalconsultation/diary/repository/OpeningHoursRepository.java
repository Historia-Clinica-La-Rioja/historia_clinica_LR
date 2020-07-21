package net.pladema.medicalconsultation.diary.repository;

import net.pladema.medicalconsultation.diary.repository.entity.OpeningHours;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OpeningHoursRepository extends JpaRepository<OpeningHours, Integer> {
}
