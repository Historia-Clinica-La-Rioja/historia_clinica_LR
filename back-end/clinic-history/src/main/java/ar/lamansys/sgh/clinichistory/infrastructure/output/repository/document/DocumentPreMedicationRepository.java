package ar.lamansys.sgh.clinichistory.infrastructure.output.repository.document;

import ar.lamansys.sgh.clinichistory.domain.ips.PreMedicationBo;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.document.entity.DocumentPreMedication;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.document.entity.DocumentPreMedicationPK;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface DocumentPreMedicationRepository extends JpaRepository<DocumentPreMedication, DocumentPreMedicationPK> {

    @Transactional(readOnly = true)
    @Query(value = "SELECT new ar.lamansys.sgh.clinichistory.domain.ips.PreMedicationBo(" +
            "s.sctid, s.pt, q.value, q.unit, d.periodUnit, d.startDate, pm.viaId)" +
            "FROM DocumentPreMedication dpm " +
            "JOIN PreMedication pm ON (dpm.documentPreMedicationPK.preMedicationId = pm.id) " +
            "JOIN Snomed s ON (pm.snomedId = s.id) " +
            "JOIN Dosage d ON (pm.dosageId = d.id) " +
            "JOIN Quantity q ON (d.doseQuantityId = q.id) " +
            "WHERE dpm.documentPreMedicationPK.documentId = :documentId")
    List<PreMedicationBo> getPreMedicationStateFromDocument(@Param("documentId") Long documentId);
}
