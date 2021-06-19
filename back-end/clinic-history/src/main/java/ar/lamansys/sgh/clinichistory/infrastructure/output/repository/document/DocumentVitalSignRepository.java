package ar.lamansys.sgh.clinichistory.infrastructure.output.repository.document;

import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.document.entity.DocumentVitalSign;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.document.entity.DocumentVitalSignPK;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.hospitalizationState.entity.ClinicalObservationVo;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.masterdata.entity.ObservationStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface DocumentVitalSignRepository extends JpaRepository<DocumentVitalSign, DocumentVitalSignPK> {


    @Transactional(readOnly = true)
    @Query("SELECT NEW ar.lamansys.sgh.clinichistory.infrastructure.output.repository.hospitalizationState.entity.ClinicalObservationVo(" +
            "ovs.id, s.sctid, ovs.statusId, ovs.value, ovs.effectiveTime) " +
            "FROM DocumentVitalSign dvs " +
            "JOIN ObservationVitalSign ovs ON (dvs.pk.observationVitalSignId = ovs.id) " +
            "JOIN Snomed s ON (ovs.snomedId = s.id) " +
            "WHERE dvs.pk.documentId = :documentId " +
            "AND ovs.statusId NOT IN ('"+ ObservationStatus.ERROR+"')")
    List<ClinicalObservationVo> getVitalSignStateFromDocument(@Param("documentId") Long documentId);

}
