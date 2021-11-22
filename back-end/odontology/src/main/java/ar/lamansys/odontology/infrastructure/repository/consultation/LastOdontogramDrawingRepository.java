package ar.lamansys.odontology.infrastructure.repository.consultation;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface LastOdontogramDrawingRepository extends JpaRepository<LastOdontogramDrawing, LastOdontogramDrawingPK> {

    @Query("SELECT lod " +
            "FROM LastOdontogramDrawing lod " +
            "WHERE lod.pk.patientId = :patientId ")
    List<LastOdontogramDrawing> getByPatientId(@Param("patientId") Integer patientId);

}
