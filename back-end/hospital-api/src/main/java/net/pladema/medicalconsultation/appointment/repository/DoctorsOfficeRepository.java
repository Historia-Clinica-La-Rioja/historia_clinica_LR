package net.pladema.medicalconsultation.appointment.repository;

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
    @Query("SELECT d "+
            "FROM DoctorsOffice AS d " +
            "WHERE d.institutionId = :institutionId ")
    List<DoctorsOffice> getDoctorOfficesByInstitution(@Param("institutionId") Integer institutionId);

    @Transactional(readOnly = true)
    @Query("SELECT d.institutionId "+
            "FROM DoctorsOffice AS d " +
            "WHERE d.id = :id ")
    Integer getInstitutionId(@Param("id") Integer id);

    @Transactional(readOnly = true)
    @Query("SELECT d.id "+
            "FROM DoctorsOffice AS d " +
            "WHERE d.institutionId IN :institutionsIds ")
    List<Integer> getAllIdsByInstitutionsId(@Param("institutionsIds") List<Integer> institutionsIds);

}
