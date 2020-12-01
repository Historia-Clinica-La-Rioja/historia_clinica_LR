package net.pladema.clinichistory.documents.repository;

import net.pladema.clinichistory.documents.repository.entity.DocumentVitalSign;
import net.pladema.clinichistory.documents.repository.entity.DocumentVitalSignPK;
import net.pladema.clinichistory.documents.repository.generalstate.domain.ClinicalObservationVo;
import net.pladema.clinichistory.documents.repository.ips.masterdata.entity.ObservationStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface DocumentVitalSignRepository extends JpaRepository<DocumentVitalSign, DocumentVitalSignPK> {


    @Transactional(readOnly = true)
    @Query("SELECT NEW net.pladema.clinichistory.documents.repository.generalstate.domain.ClinicalObservationVo(" +
            "ovs.id, ovs.sctidCode, ovs.statusId, ovs.value, ovs.effectiveTime) " +
            "FROM DocumentVitalSign dvs " +
            "JOIN ObservationVitalSign ovs ON (dvs.pk.observationVitalSignId = ovs.id) " +
            "WHERE dvs.pk.documentId = :documentId " +
            "AND ovs.statusId NOT IN ('"+ ObservationStatus.ERROR+"')")
    List<ClinicalObservationVo> getVitalSignStateFromDocument(@Param("documentId") Long documentId);

}
