package net.pladema.medicalconsultation.doctorsoffice.repository;

import net.pladema.medicalconsultation.doctorsoffice.repository.domain.DoctorsOfficeVo;
import net.pladema.medicalconsultation.doctorsoffice.repository.entity.DoctorsOffice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface DoctorsOfficeRepository extends JpaRepository<DoctorsOffice, Integer> {


    @Transactional(readOnly = true)
    @Query("SELECT NEW net.pladema.medicalconsultation.doctorsoffice.repository.domain.DoctorsOfficeVo(" +
            "do.id, do.description, do.openingTime, do.closingTime) " +
            "FROM DoctorsOffice do " +
            "JOIN ClinicalSpecialtySector css on ( do.clinicalSpecialtySectorId = css.id ) " +
            "WHERE do.institutionId = :institutionId " +
            "AND css.sectorId = :sectorId " +
            "AND css.clinicalSpecialtyId = :clinicalSpecialtyId " +
            "ORDER BY do.description ASC ")
    List<DoctorsOfficeVo> findAllBy(@Param("institutionId") Integer institutionId,
                                    @Param("sectorId") Integer sectorId,
                                    @Param("clinicalSpecialtyId") Integer clinicalSpecialtyId);
}
