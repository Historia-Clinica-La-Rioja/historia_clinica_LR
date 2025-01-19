package ar.lamansys.sgh.clinichistory.infrastructure.output.repository.document;

import ar.lamansys.sgh.clinichistory.domain.ips.PostAnesthesiaStatusBo;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.document.entity.DocumentPostAnesthesiaStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface DocumentPostAnesthesiaStatusRepository extends JpaRepository<DocumentPostAnesthesiaStatus, Long> {

    @Transactional(readOnly = true)
    @Query(value = "SELECT new ar.lamansys.sgh.clinichistory.domain.ips.PostAnesthesiaStatusBo(" +
            "dpas.documentId, dpas.intentionalSensitivity, dpas.cornealReflex, dpas.obeyOrders, dpas.talk," +
            "dpas.respiratoryDepression, dpas.circulatoryDepression, dpas.vomiting, dpas.curated, dpas.trachealCannula," +
            "dpas.pharyngealCannula, dpas.internment, dpas.internmentPlaceId, n.description) " +
            "FROM DocumentPostAnesthesiaStatus dpas " +
            "LEFT JOIN Note n ON (dpas.noteId = n.id) " +
            "WHERE dpas.documentId = :documentId")
    PostAnesthesiaStatusBo getDocumentPostAnesthesiaStatus(@Param("documentId") Long documentId);

}
