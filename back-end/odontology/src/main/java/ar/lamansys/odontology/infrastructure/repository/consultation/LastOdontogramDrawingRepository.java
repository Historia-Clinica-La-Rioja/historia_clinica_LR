package ar.lamansys.odontology.infrastructure.repository.consultation;

import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.document.DocumentType;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
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

	@Transactional
	@Modifying
	@Query("DELETE FROM LastOdontogramDrawing lod " +
			"WHERE lod.patientId = :patientId ")
	void deleteByPatientId(@Param("patientId") Integer patientId);

	@Transactional(readOnly = true)
	@Query("SELECT hod.toothId " +
			" FROM HistoricOdontogramDrawing hod" +
			" JOIN OdontologyConsultation oc ON oc.id = hod.odontologyConsultationId" +
			" JOIN Document d ON oc.id = d.sourceId" +
			" JOIN DocumentHealthCondition dhc ON d.id = dhc.pk.documentId" +
			" WHERE d.typeId = '" + DocumentType.ODONTOLOGY + "'" +
			" AND dhc.pk.healthConditionId = :healthConditionId")
	List<String> getToothIdByHealthConditionId(@Param("healthConditionId") Integer healthConditionId);

	@Transactional
	@Modifying
	@Query("DELETE FROM LastOdontogramDrawing lod " +
			"WHERE lod.patientId = :patientId " +
			"AND lod.toothId = :toothId ")
	void deleteByPatientIdAndToothId(@Param("patientId") Integer patientId, @Param("toothId") String toothId);

}
