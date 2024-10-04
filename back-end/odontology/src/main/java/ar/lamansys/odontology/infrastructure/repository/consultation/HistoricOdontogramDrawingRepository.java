package ar.lamansys.odontology.infrastructure.repository.consultation;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.document.DocumentType;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.document.SourceType;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.masterdata.entity.ConditionClinicalStatus;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface HistoricOdontogramDrawingRepository extends JpaRepository<HistoricOdontogramDrawing, Integer> {

	@Transactional(readOnly = true)
	@Query("SELECT hod " +
			"FROM HistoricOdontogramDrawing hod " +
			"JOIN OdontologyConsultation oc ON oc.id = hod.odontologyConsultationId " +
			"JOIN Document d ON oc.id = d.sourceId AND oc.patientId = d.patientId " +
			"JOIN DocumentHealthCondition dhc ON d.id = dhc.pk.documentId " +
			"JOIN HealthCondition hc ON dhc.pk.healthConditionId = hc.id " +
			"JOIN Snomed s ON s.id = hc.snomedId " +
			"WHERE hc.statusId = '" + ConditionClinicalStatus.ACTIVE + "' " +
			"AND hod.toothId = :toothId " +
			"AND hod.patientId = :patientId " +
			"AND hc.patientId = :patientId " +
			"AND d.typeId = '" + DocumentType.ODONTOLOGY + "' " +
			"AND d.sourceTypeId = '" + SourceType.ODONTOLOGY + "' " +
			"AND s.sctid LIKE :sctid " +
			"GROUP BY hod.id " +
			"ORDER BY hod.id DESC")
	Page<HistoricOdontogramDrawing> getLastActiveHistoricOdontogramDrawingByPatientAndTooth(
			@Param("patientId") Integer patientId,
			@Param("toothId") String toothId,
			@Param("sctid") String sctid,
			Pageable pageable);
}
