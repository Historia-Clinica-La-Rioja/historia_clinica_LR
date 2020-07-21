package net.pladema.medicalconsultation.diary.repository;

import net.pladema.medicalconsultation.diary.repository.entity.DiaryOpeningHours;
import net.pladema.medicalconsultation.diary.repository.entity.DiaryOpeningHoursPK;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DiaryOpeningHoursRepository extends JpaRepository<DiaryOpeningHours, DiaryOpeningHoursPK> {
}
