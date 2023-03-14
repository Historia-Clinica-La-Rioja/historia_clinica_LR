package ar.lamansys.odontology.infrastructure.repository.consultation;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

public interface LastOdontogramDrawingRepository extends JpaRepository<LastOdontogramDrawing, Integer> {

    @Query("SELECT lod " +
            "FROM LastOdontogramDrawing lod " +
            "WHERE lod.patientId = :patientId ")
    List<LastOdontogramDrawing> getByPatientId(@Param("patientId") Integer patientId);

	@Transactional(readOnly = true)
	@Query("SELECT lod " +
			"FROM LastOdontogramDrawing lod " +
			"WHERE lod.patientId = :patientId " +
			"AND lod.toothId = :toothId")
	Optional<LastOdontogramDrawing> getByPatientToothId(@Param("patientId") Integer patientId, @Param("toothId") String toothId);

}
