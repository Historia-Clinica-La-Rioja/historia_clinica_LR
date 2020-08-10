package net.pladema.medicalconsultation.diary.repository;

import net.pladema.medicalconsultation.diary.repository.domain.CompleteDiaryListVo;
import net.pladema.medicalconsultation.diary.repository.domain.DiaryListVo;
import net.pladema.medicalconsultation.diary.repository.entity.Diary;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

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
    @Query("select id " +
            "from Diary " +
            "where healthcareProfessionalId = :hcpId " +
            "and doctorsOfficeId = :doId " +
            "and startDate <= :endDate " +
            "and endDate >= :startDate " +
            "AND id != :excludeDiaryId ")
    List<Integer> findAllOverlappingDiaryExcluding(@Param("hcpId") Integer healthcareProfessionalId,
                                          @Param("doId") Integer doctorsOfficeId,
                                          @Param("startDate") LocalDate newDiaryStart,
                                          @Param("endDate") LocalDate newDiaryEnd, 
                                          @Param("excludeDiaryId") Integer excludeDiaryId);


    @Transactional(readOnly = true)
    @Query("SELECT NEW net.pladema.medicalconsultation.diary.repository.domain.DiaryListVo(" +
            "d, do.description) " +
            "FROM Diary d " +
            "JOIN DoctorsOffice AS do ON (do.id = d.doctorsOfficeId) " +
            "WHERE d.healthcareProfessionalId = :hcpId " +
            "AND d.active = true")
    List<DiaryListVo> getActiveDiariesFromProfessional(@Param("hcpId") Integer healthcareProfessionalId);
    
    @Transactional(readOnly = true)
    @Query("SELECT NEW net.pladema.medicalconsultation.diary.repository.domain.CompleteDiaryListVo( " +
            "d, do.description, css.sectorId, css.clinicalSpecialtyId, d.healthcareProfessionalId) " +
            "FROM Diary d " +
            "JOIN DoctorsOffice do ON do.id = d.doctorsOfficeId " +
            "JOIN ClinicalSpecialtySector css ON css.id = do.clinicalSpecialtySectorId " +
            "WHERE d.id = :diaryId ")
    Optional<CompleteDiaryListVo> getDiary(@Param("diaryId") Integer diaryId);
}
