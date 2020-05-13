package net.pladema.internation.repository.core;

import net.pladema.internation.repository.core.entity.DocumentVitalSign;
import net.pladema.internation.repository.core.entity.DocumentVitalSignPK;
import net.pladema.internation.repository.ips.generalstate.ClinicalObservationVo;
import net.pladema.internation.repository.masterdata.entity.ObservationStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface DocumentVitalSignRepository extends JpaRepository<DocumentVitalSign, DocumentVitalSignPK> {


    @Transactional(readOnly = true)
    @Query("SELECT NEW net.pladema.internation.repository.ips.generalstate.ClinicalObservationVo(" +
            "ovs.id, ovs.sctidCode, ovs.statusId, ovs.value, ovs.effectiveTime) " +
            "FROM DocumentVitalSign dvs " +
            "JOIN ObservationVitalSign ovs ON (dvs.pk.observationVitalSignId = ovs.id) " +
            "WHERE dvs.pk.documentId = :documentId " +
            "AND ovs.statusId NOT IN ('"+ ObservationStatus.ERROR+"')")
    List<ClinicalObservationVo> getVitalSignStateFromDocument(@Param("documentId") Long documentId);

}
