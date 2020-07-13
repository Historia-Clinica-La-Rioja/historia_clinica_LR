package net.pladema.appointment.repository;

import net.pladema.appointment.repository.entity.DiaryOpeningHours;
import net.pladema.appointment.repository.entity.DiaryOpeningHoursPK;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DiaryOpeningHoursRepository extends JpaRepository<DiaryOpeningHours, DiaryOpeningHoursPK> {
}
