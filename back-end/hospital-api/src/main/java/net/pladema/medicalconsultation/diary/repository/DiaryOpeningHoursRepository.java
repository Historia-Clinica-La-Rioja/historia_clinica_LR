package net.pladema.medicalconsultation.diary.repository;

import net.pladema.medicalconsultation.diary.repository.domain.OccupationVo;
import net.pladema.medicalconsultation.diary.repository.entity.DiaryOpeningHours;
import net.pladema.medicalconsultation.diary.repository.entity.DiaryOpeningHoursPK;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface DiaryOpeningHoursRepository extends JpaRepository<DiaryOpeningHours, DiaryOpeningHoursPK> {

    @Query("select new net.pladema.medicalconsultation.diary.repository.domain.OccupationVo( " +
            "d.id, d.startDate, d.endDate, oh.dayWeekId, oh.from, oh.to) " +
            "from DiaryOpeningHours as doh " +
            "join Diary as d on ( doh.pk.diaryId = d.id ) " +
            "join OpeningHours as oh on ( doh.pk.openingHoursId = oh.id ) " +
            "where d.doctorsOfficeId = :doctorsOfficeId " +
            "and d.startDate <= :endDate " +
            "and d.endDate >= :startDate " +
            "order by oh.dayWeekId, oh.from")
    List<OccupationVo> findAllWeeklyDoctorsOfficeOcupation(@Param("doctorsOfficeId") Integer doctorsOfficeId,
                                                           @Param("startDate")LocalDate startDate,
                                                           @Param("endDate")LocalDate endDate);
}
