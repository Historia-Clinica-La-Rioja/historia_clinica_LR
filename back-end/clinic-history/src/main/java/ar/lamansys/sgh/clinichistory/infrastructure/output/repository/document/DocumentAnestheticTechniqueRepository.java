package ar.lamansys.sgh.clinichistory.infrastructure.output.repository.document;

import ar.lamansys.sgh.clinichistory.domain.ips.AnestheticTechniqueBo;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.document.entity.DocumentAnestheticTechnique;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.document.entity.DocumentAnestheticTechniquePK;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface DocumentAnestheticTechniqueRepository extends JpaRepository<DocumentAnestheticTechnique, DocumentAnestheticTechniquePK> {

    @Transactional(readOnly = true)
    @Query(value = "SELECT new ar.lamansys.sgh.clinichistory.domain.ips.AnestheticTechniqueBo(" +
            "at.id, s.sctid, s.pt, at.techniqueId, at.trachealIntubation, at.breathingId, at.circuitId)" +
            "FROM DocumentAnestheticTechnique dat " +
            "JOIN AnestheticTechnique at ON (dat.documentAnestheticTechniquePK.anestheticTechniqueId = at.id) " +
            "JOIN Snomed s ON (at.snomedId = s.id) " +
            "WHERE dat.documentAnestheticTechniquePK.documentId = :documentId")
    List<AnestheticTechniqueBo> getAnestheticTechniquesStateFromDocument(@Param("documentId") Long documentId);

    @Transactional(readOnly = true)
    @Query(value = "SELECT ti.trachealIntubationPK.trachealIntubationId " +
            "FROM AnestheticTechnique at " +
            "JOIN TrachealIntubation ti ON (at.id = ti.trachealIntubationPK.anestheticTechniqueId) " +
            "WHERE at.id = :anestheticTechniqueId")
    List<Short> getTrachealIntubationState(@Param("anestheticTechniqueId") Integer anestheticTechniqueId);
}
