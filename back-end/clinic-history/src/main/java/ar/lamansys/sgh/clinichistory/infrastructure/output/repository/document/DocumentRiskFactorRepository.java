package ar.lamansys.sgh.clinichistory.infrastructure.output.repository.document;

import java.util.List;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.ips.entity.OtherRiskFactorVo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.document.entity.DocumentRiskFactor;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.document.entity.DocumentRiskFactorPK;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.hospitalizationState.entity.ClinicalObservationVo;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.masterdata.entity.ObservationStatus;

@Repository
public interface DocumentRiskFactorRepository extends JpaRepository<DocumentRiskFactor, DocumentRiskFactorPK> {


    @Transactional(readOnly = true)
    @Query("SELECT NEW ar.lamansys.sgh.clinichistory.infrastructure.output.repository.hospitalizationState.entity.ClinicalObservationVo(" +
            "ovs.id, s.sctid, ovs.statusId, ovs.value, ovs.effectiveTime) " +
            "FROM DocumentRiskFactor drf " +
            "JOIN ObservationRiskFactor ovs ON (drf.pk.observationRiskFactorId = ovs.id) " +
            "JOIN Snomed s ON (ovs.snomedId = s.id) " +
            "WHERE drf.pk.documentId = :documentId " +
            "AND ovs.statusId NOT IN ('"+ ObservationStatus.ERROR+"')")
    List<ClinicalObservationVo> getRiskFactorStateFromDocument(@Param("documentId") Long documentId);

	@Transactional(readOnly = true)
	@Query("SELECT DISTINCT NEW ar.lamansys.sgh.clinichistory.infrastructure.output.repository.ips.entity.OtherRiskFactorVo(" +
			"td.bodyTemperatureId, td.cryingExcessive, td.muscleHypertoniaId, td.respiratoryRetractionId, td.stridor, td.perfusionId) " +
			"FROM DocumentRiskFactor drf " +
			"JOIN ObservationRiskFactor ovs ON (drf.pk.observationRiskFactorId = ovs.id)" +
			"JOIN TriageRiskFactors trf ON (trf.pk.observationRiskFactorId = ovs.id) " +
			"JOIN TriageDetails td ON (td.triageId = trf.pk.triageId) " +
			"WHERE drf.pk.documentId = :documentId " +
			"AND ovs.statusId NOT IN ('"+ ObservationStatus.ERROR+"')")
	OtherRiskFactorVo getOtherRiskFactorsFromDocument(@Param("documentId") Long documentId);

}
