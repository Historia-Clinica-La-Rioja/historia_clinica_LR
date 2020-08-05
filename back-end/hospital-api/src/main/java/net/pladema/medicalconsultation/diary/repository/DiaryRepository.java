package net.pladema.medicalconsultation.diary.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import net.pladema.medicalconsultation.diary.repository.domain.CompleteDiaryListVo;
import net.pladema.medicalconsultation.diary.repository.domain.DiaryListVo;
import net.pladema.medicalconsultation.diary.repository.entity.Diary;

@Repository
public interface DiaryRepository extends JpaRepository<Diary, Integer> {


    @Transactional(readOnly = true)
    @Query("select id " +
            "from Diary " +
            "where healthcareProfessionalId = :hcpId " +
            "and doctorsOfficeId = :doId " +
            "and startDate <= :endDate " +
            "and endDate >= :startDate")
    List<Integer> findAllOverlappingDiary(@Param("hcpId") Integer healthcareProfessionalId,
                                          @Param("doId") Integer doctorsOfficeId,
                                          @Param("startDate") LocalDate newDiaryStart,
                                          @Param("endDate") LocalDate newDiaryEnd);


    @Transactional(readOnly = true)
    @Query("SELECT NEW net.pladema.medicalconsultation.diary.repository.domain.DiaryListVo( " +
            "d.id, d.doctorsOfficeId, d.startDate, d.endDate, d.appointmentDuration, d.professionalAsignShift, " +
            "d.includeHoliday)" +
            "FROM Diary d " +
            "WHERE d.healthcareProfessionalId = :hcpId " +
            "AND d.active = true")
    List<DiaryListVo> getActiveDiariesFromProfessional(@Param("hcpId") Integer healthcareProfessionalId);
    
    @Transactional(readOnly = true)
    @Query("SELECT NEW net.pladema.medicalconsultation.diary.repository.domain.CompleteDiaryListVo( " +
            "d.id, d.doctorsOfficeId, d.startDate, d.endDate, d.appointmentDuration, d.professionalAsignShift, " +
            "d.includeHoliday, css.sectorId, css.clinicalSpecialtyId) " +
            "FROM Diary d " +
            "JOIN DoctorsOffice do ON do.id = d.doctorsOfficeId " +
            "JOIN ClinicalSpecialtySector css ON css.id = do.clinicalSpecialtySectorId " +
            "WHERE d.id = :diaryId ")
    Optional<CompleteDiaryListVo> getDiary(@Param("diaryId") Integer diaryId);
}
