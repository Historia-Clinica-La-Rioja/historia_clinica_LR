package ar.lamansys.odontology.infrastructure.repository.consultation;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ToothIndicesRepository extends JpaRepository<ToothIndices, ToothIndicesPK>  {

    @Query("SELECT ti " +
           "FROM ToothIndices ti " +
           "WHERE ti.pk.patientId = :patientId ")
    List<ToothIndices> getByPatientId(@Param("patientId") Integer patientId);

}
