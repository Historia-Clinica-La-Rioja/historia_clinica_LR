package net.pladema.internation.repository.core;

import net.pladema.internation.repository.core.entity.DocumentLab;
import net.pladema.internation.repository.core.entity.DocumentLabPK;
import net.pladema.internation.repository.ips.generalstate.ClinicalObservationVo;
import net.pladema.internation.repository.masterdata.entity.ObservationStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface DocumentLabRepository extends JpaRepository<DocumentLab, DocumentLabPK> {

    @Transactional(readOnly = true)
    @Query("SELECT NEW net.pladema.internation.repository.ips.generalstate.ClinicalObservationVo(" +
            "ol.id, ol.sctidCode, ol.statusId, ol.value, ol.effectiveTime) " +
            "FROM DocumentLab dl " +
            "JOIN ObservationLab ol ON (dl.pk.observationLabId = ol.id) " +
            "WHERE dl.pk.documentId = :documentId " +
            "AND ol.statusId NOT IN ('"+ ObservationStatus.ERROR+"')")
    List<ClinicalObservationVo> getLabStateFromDocument(@Param("documentId") Long documentId);

}
