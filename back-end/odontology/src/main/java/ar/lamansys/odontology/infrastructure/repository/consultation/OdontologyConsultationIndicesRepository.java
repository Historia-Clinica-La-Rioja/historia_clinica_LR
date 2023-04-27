package ar.lamansys.odontology.infrastructure.repository.consultation;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

public interface OdontologyConsultationIndicesRepository extends JpaRepository<OdontologyConsultationIndices, Integer> {

    @Query("SELECT oci " +
            "FROM OdontologyConsultationIndices oci " +
            "JOIN OdontologyConsultation oc ON (oci.odontologyConsultationId = oc.id) " +
            "WHERE oc.patientId = :patientId " +
            "ORDER BY oci.date DESC ")
    List<OdontologyConsultationIndices> getByPatientId(@Param("patientId") Integer patientId);

	@Transactional(readOnly = true)
	@Query("SELECT oci " +
			"FROM OdontologyConsultationIndices oci " +
			"WHERE oci.odontologyConsultationId = :ocId ")
	Optional<OdontologyConsultationIndices> getByOdontologyConsultationId(@Param("ocId") Integer ocId);
}
