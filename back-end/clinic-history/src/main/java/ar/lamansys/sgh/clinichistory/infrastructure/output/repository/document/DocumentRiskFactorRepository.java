package ar.lamansys.sgh.clinichistory.infrastructure.output.repository.document;

import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.document.entity.DocumentRiskFactor;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.document.entity.DocumentRiskFactorPK;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.hospitalizationState.entity.ClinicalObservationVo;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.masterdata.entity.ObservationStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

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

}
