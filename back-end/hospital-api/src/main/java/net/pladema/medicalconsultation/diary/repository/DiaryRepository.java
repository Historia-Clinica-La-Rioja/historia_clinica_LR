package net.pladema.medicalconsultation.diary.repository;

import net.pladema.medicalconsultation.diary.repository.entity.Diary;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

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

}
