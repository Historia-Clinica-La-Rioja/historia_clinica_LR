package ar.lamansys.online.infraestructure.output.repository;

import ar.lamansys.online.infraestructure.output.entity.MandatoryMedicalPractice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
public interface BookingPracticeRepository extends JpaRepository<MandatoryMedicalPractice, Integer> {

    @Transactional(readOnly = true)
    @Query("SELECT mmp " +
    "FROM MandatoryMedicalPractice mmp " +
    "WHERE mmp.snomedId = :snomedId")
    Optional<MandatoryMedicalPractice> findBySnomedId(@Param("snomedId")Integer snomedId);
}
