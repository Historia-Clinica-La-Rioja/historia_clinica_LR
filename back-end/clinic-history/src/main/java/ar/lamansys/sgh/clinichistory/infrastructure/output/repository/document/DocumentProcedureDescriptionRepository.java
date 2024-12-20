package ar.lamansys.sgh.clinichistory.infrastructure.output.repository.document;

import ar.lamansys.sgh.clinichistory.domain.ips.ProcedureDescriptionBo;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.document.entity.DocumentProcedureDescription;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface DocumentProcedureDescriptionRepository extends JpaRepository<DocumentProcedureDescription, Long> {

    @Transactional(readOnly = true)
    @Query(value = "SELECT new ar.lamansys.sgh.clinichistory.domain.ips.ProcedureDescriptionBo(" +
            "dpd.documentId, n.description, dpd.asa, dpd.venousAccess, dpd.nasogastricTube, dpd.urinaryCatheter, dpd.foodIntake, " +
            "dpd.foodIntakeDate, dpd.anesthesiaStartDate, dpd.anesthesiaStartTime, dpd.anesthesiaEndDate, dpd.anesthesiaEndTime, " +
            "dpd.surgeryStartDate, dpd.surgeryStartTime, dpd.surgeryEndDate, dpd.surgeryEndTime) " +
            "FROM DocumentProcedureDescription dpd " +
            "LEFT JOIN Note n ON (dpd.noteId = n.id) " +
            "WHERE dpd.documentId = :documentId")
    ProcedureDescriptionBo getDocumentProcedureDescription(@Param("documentId") Long documentId);

}
