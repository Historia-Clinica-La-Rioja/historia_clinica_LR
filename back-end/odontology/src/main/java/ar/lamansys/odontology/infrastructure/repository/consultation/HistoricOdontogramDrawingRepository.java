package ar.lamansys.odontology.infrastructure.repository.consultation;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.masterdata.entity.ConditionClinicalStatus;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface HistoricOdontogramDrawingRepository extends JpaRepository<HistoricOdontogramDrawing, Integer> {

	@Query("SELECT hod" +
			" FROM HistoricOdontogramDrawing hod" +
			" JOIN OdontologyConsultation oc ON oc.id = hod.odontologyConsultationId" +
			" JOIN Document d ON oc.id = d.sourceId" +
			" JOIN DocumentHealthCondition dhc ON d.id = dhc.pk.documentId" +
			" JOIN HealthCondition hc ON dhc.pk.healthConditionId = hc.id" +
			" WHERE hc.statusId = '" + ConditionClinicalStatus.ACTIVE + "'" +
			" AND hod.toothId = :toothId" +
			" AND hod.patientId = :patientId" +
			" AND hc.patientId = :patientId" +
			" GROUP BY hod.id" +
			" ORDER BY hod.id DESC")
	Page<HistoricOdontogramDrawing> getLastActiveHistoricOdontogramDrawingByPatientAndTooth(@Param("patientId") Integer patientId,
																							@Param("toothId") String toothId,
																							Pageable pageable);
}
