package ar.lamansys.sgh.clinichistory.infrastructure.output.repository.ips;

import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.ips.entity.OdontologyProcedure;
import ar.lamansys.sgx.shared.auditable.repository.SGXAuditableEntityJPARepository;
import ar.lamansys.sgx.shared.migratable.SGXDocumentEntityRepository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface OdontologyProcedureRepository extends SGXAuditableEntityJPARepository<OdontologyProcedure, Integer>, SGXDocumentEntityRepository<OdontologyProcedure> {

	@Override
	@Transactional(readOnly = true)
	@Query("SELECT op " +
			"FROM DocumentOdontologyProcedure dop " +
			"JOIN OdontologyProcedure op ON (dop.pk.odontologyProcedureId = op.id) " +
			"WHERE dop.pk.documentId IN :documentIds ")
	List<OdontologyProcedure> getEntitiesByDocuments(@Param("documentIds") List<Long> documentIds);

	@Transactional(readOnly = true)
	@Query("SELECT op " +
			"FROM OdontologyProcedure op " +
			"WHERE op.patientId = :patientId " +
			"ORDER BY op.performedDate")
	List<OdontologyProcedure> findAllByPatientId(@Param("patientId") Integer patientId);

}
