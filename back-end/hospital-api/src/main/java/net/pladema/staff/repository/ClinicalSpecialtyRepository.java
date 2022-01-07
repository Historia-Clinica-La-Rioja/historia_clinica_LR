package net.pladema.staff.repository;

import net.pladema.staff.service.domain.ClinicalSpecialtyBo;
import net.pladema.staff.repository.entity.ClinicalSpecialty;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface ClinicalSpecialtyRepository extends JpaRepository<ClinicalSpecialty, Integer>{

    @Transactional(readOnly = true)
    @Query(value = " SELECT cs FROM HealthcareProfessionalSpecialty hps "
            + "INNER JOIN ClinicalSpecialty cs ON hps.clinicalSpecialtyId = cs.id "
            + "WHERE hps.healthcareProfessionalId = :professionalId")
    List<ClinicalSpecialty> getAllByProfessional(@Param("professionalId") Integer professionalId);

    @Transactional(readOnly = true)
    @Query(value = " SELECT cs FROM Diary d " +
            "JOIN DoctorsOffice doff " +
            "ON d.doctorsOfficeId = doff.id " +
            "JOIN ClinicalSpecialtySector css " +
            "ON doff.clinicalSpecialtySectorId = css.id " +
            "JOIN ClinicalSpecialty cs "+
            "ON cs.id = css.clinicalSpecialtyId " +
            "WHERE d.id = :diaryId")
    ClinicalSpecialty getClinicalSpecialtyByDiary(@Param("diaryId") Integer diaryId);

    @Transactional(readOnly = true)
    @Query(value = " SELECT new net.pladema.staff.service.domain.ClinicalSpecialtyBo(cs.id, cs.name) " +
            "FROM ClinicalSpecialty cs " +
            "WHERE cs.id = :clinicalSpecialtyId")
    ClinicalSpecialtyBo findClinicalSpecialtyBoById(@Param("clinicalSpecialtyId") Integer clinicalSpecialtyId);

    @Transactional(readOnly = true)
    @Query(value = " SELECT cs FROM ClinicalSpecialty cs where cs.clinicalSpecialtyTypeId = 2")
    List<ClinicalSpecialty> findAllSpecialties();

}
