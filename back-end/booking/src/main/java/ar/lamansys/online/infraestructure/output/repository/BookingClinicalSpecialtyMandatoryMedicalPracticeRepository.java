package ar.lamansys.online.infraestructure.output.repository;

import ar.lamansys.online.infraestructure.output.entity.ClinicalSpecialtyMandatoryMedicalPractice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

public interface BookingClinicalSpecialtyMandatoryMedicalPracticeRepository
        extends JpaRepository<ClinicalSpecialtyMandatoryMedicalPractice, Integer> {

    @Transactional(readOnly = true)
    @Query("SELECT csmmp " +
            "FROM MandatoryMedicalPractice mmp " +
            "JOIN ClinicalSpecialtyMandatoryMedicalPractice csmmp ON csmmp.mandatoryMedicalPracticeId = mmp.id " +
            "WHERE mmp.snomedId = :snomedId AND csmmp.clinicalSpecialtyId = :specialtyId")
	List<ClinicalSpecialtyMandatoryMedicalPractice> findBySnomedIdAndSpecialtyId(@Param("snomedId") Integer snomedId,
																				 @Param("specialtyId") Integer specialtyId);

}
