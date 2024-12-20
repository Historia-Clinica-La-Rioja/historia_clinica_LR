package net.pladema.medicalconsultation.diary.repository;

import net.pladema.medicalconsultation.diary.repository.entity.DiaryBookingRestriction;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DiaryBookingRestrictionRepository extends JpaRepository<DiaryBookingRestriction,Integer> {
}
