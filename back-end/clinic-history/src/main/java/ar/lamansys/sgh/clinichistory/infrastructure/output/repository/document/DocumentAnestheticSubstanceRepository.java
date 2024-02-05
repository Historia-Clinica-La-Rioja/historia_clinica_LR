package ar.lamansys.sgh.clinichistory.infrastructure.output.repository.document;

import ar.lamansys.sgh.clinichistory.domain.ips.AnestheticSubstanceBo;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.document.entity.DocumentAnestheticSubstance;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.document.entity.DocumentAnestheticSubstancePK;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface DocumentAnestheticSubstanceRepository extends JpaRepository<DocumentAnestheticSubstance, DocumentAnestheticSubstancePK> {

    @Transactional(readOnly = true)
    @Query(value = "SELECT new ar.lamansys.sgh.clinichistory.domain.ips.AnestheticSubstanceBo(" +
            "s.sctid, s.pt, q.value, q.unit, d.periodUnit, d.startDate, asub.viaId, n.description, asub.typeId)" +
            "FROM DocumentAnestheticSubstance dpm " +
            "JOIN AnestheticSubstance asub ON (dpm.documentAnestheticSubstancePK.anestheticSubstanceId = asub.id) " +
            "JOIN Snomed s ON (asub.snomedId = s.id) " +
            "JOIN Dosage d ON (asub.dosageId = d.id) " +
            "JOIN Quantity q ON (d.doseQuantityId = q.id) " +
            "LEFT JOIN Note n ON (asub.viaNoteId = n.id) " +
            "WHERE dpm.documentAnestheticSubstancePK.documentId = :documentId")
    List<AnestheticSubstanceBo> getAnestheticSubstancesStateFromDocument(@Param("documentId") Long documentId);
}
