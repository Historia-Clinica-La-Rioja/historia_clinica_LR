package ar.lamansys.odontology.infrastructure.repository.consultation;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface OdontologyConsultationIndicesRepository extends JpaRepository<OdontologyConsultationIndices, Integer> {

    @Query("SELECT oci " +
            "FROM OdontologyConsultationIndices oci " +
            "JOIN OdontologyConsultation oc ON (oci.odontologyConsultationId = oc.id) " +
            "WHERE oc.patientId = :patientId " +
            "ORDER BY oci.date DESC ")
    List<OdontologyConsultationIndices> getByPatientId(@Param("patientId") Integer patientId);

}
