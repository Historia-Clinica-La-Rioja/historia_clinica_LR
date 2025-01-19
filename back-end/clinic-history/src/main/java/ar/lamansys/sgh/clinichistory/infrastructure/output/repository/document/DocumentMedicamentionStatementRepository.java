package ar.lamansys.sgh.clinichistory.infrastructure.output.repository.document;

import java.util.List;

import ar.lamansys.sgh.clinichistory.domain.GetCommercialStatementCommercialPrescriptionBo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.document.entity.DocumentMedicamentionStatement;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.document.entity.DocumentMedicamentionStatementPK;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.hospitalizationState.entity.MedicationVo;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.masterdata.entity.MedicationStatementStatus;

@Repository
public interface DocumentMedicamentionStatementRepository extends JpaRepository<DocumentMedicamentionStatement, DocumentMedicamentionStatementPK> {


    @Transactional(readOnly = true)
    @Query("SELECT NEW ar.lamansys.sgh.clinichistory.infrastructure.output.repository.hospitalizationState.entity.MedicationVo(" +
            "ms.id, s, ms.statusId, " +
            "n.id as noteId, n.description as note, ms.prescriptionLineNumber, do2, qua.value, qua.unit, s2) " +
            "FROM DocumentMedicamentionStatement dm " +
            "JOIN MedicationStatement ms ON (dm.pk.medicationStatementId = ms.id) " +
            "JOIN Snomed s ON (s.id = ms.snomedId) " +
            "LEFT JOIN Note n ON (n.id = ms.noteId) " +
			"LEFT JOIN Dosage do2 ON (do2.id = ms.dosageId) " +
			"LEFT JOIN Quantity qua ON (qua.id = do2.doseQuantityId) " +
			"LEFT JOIN HealthCondition hc ON (hc.id = ms.healthConditionId) " +
			"LEFT JOIN Snomed s2 ON (s2.id = hc.snomedId) " +
            "WHERE dm.pk.documentId = :documentId " +
            "AND ms.statusId NOT IN ('"+ MedicationStatementStatus.ERROR+"')")
    List<MedicationVo> getMedicationStateFromDocument(@Param("documentId") Long documentId);

    @Transactional(readOnly = true)
    @Query("SELECT NEW ar.lamansys.sgh.clinichistory.infrastructure.output.repository.hospitalizationState.entity.MedicationVo(" +
            "ms.id, s, ms.statusId, mss.description as status, " +
            "n.id as noteId, n.description as note) " +
            "FROM DocumentMedicamentionStatement dm " +
            "JOIN MedicationStatement ms ON (dm.pk.medicationStatementId = ms.id) " +
            "JOIN Snomed s ON (s.id = ms.snomedId) " +
            "JOIN MedicationStatementStatus mss ON (mss.id = ms.statusId) " +
            "LEFT JOIN Note n ON (n.id = ms.noteId) " +
            "WHERE dm.pk.documentId = :documentId ")
    List<MedicationVo> getMedicationStateFromDocumentToReport(@Param("documentId") Long documentId);

    @Transactional(readOnly = true)
    @Query("SELECT dm " +
            "FROM DocumentMedicamentionStatement dm " +
            "WHERE dm.pk.medicationStatementId = :mid ")
    DocumentMedicamentionStatement findByMedicationId(@Param("mid")  Integer mid);


	@Transactional(readOnly = true)
	@Query("SELECT NEW ar.lamansys.sgh.clinichistory.domain.GetCommercialStatementCommercialPrescriptionBo(mscp.medicationStatementId, mscp.presentationUnitQuantity, mscp.medicationPackQuantity) " +
			"FROM DocumentMedicamentionStatement dm " +
			"JOIN MedicationStatementCommercialPrescription mscp ON (mscp.id = dm.pk.medicationStatementId) " +
			"WHERE dm.pk.documentId = :documentId")
	List<GetCommercialStatementCommercialPrescriptionBo> fetchCommercialStatementCommercialPrescriptionByDocumentId(@Param("documentId") Long documentId);

}
