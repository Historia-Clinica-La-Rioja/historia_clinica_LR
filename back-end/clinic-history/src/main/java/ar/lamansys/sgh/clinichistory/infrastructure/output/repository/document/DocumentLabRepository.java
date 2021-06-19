package ar.lamansys.sgh.clinichistory.infrastructure.output.repository.document;

import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.document.entity.DocumentLab;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.document.entity.DocumentLabPK;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.hospitalizationState.entity.ClinicalObservationVo;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.masterdata.entity.ObservationStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface DocumentLabRepository extends JpaRepository<DocumentLab, DocumentLabPK> {

    @Transactional(readOnly = true)
    @Query("SELECT NEW ar.lamansys.sgh.clinichistory.infrastructure.output.repository.hospitalizationState.entity.ClinicalObservationVo(" +
            "ol.id, s.sctid, ol.statusId, ol.value, ol.effectiveTime) " +
            "FROM DocumentLab dl " +
            "JOIN ObservationLab ol ON (dl.pk.observationLabId = ol.id) " +
            "JOIN Snomed s ON (ol.snomedId = s.id) " +
            "WHERE dl.pk.documentId = :documentId " +
            "AND ol.statusId NOT IN ('"+ ObservationStatus.ERROR+"')")
    List<ClinicalObservationVo> getLabStateFromDocument(@Param("documentId") Long documentId);

}
