package ar.lamansys.sgh.clinichistory.infrastructure.output.repository.ips;

import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.ips.entity.Procedure;
import ar.lamansys.sgx.shared.auditable.repository.SGXAuditableEntityJPARepository;

import ar.lamansys.sgx.shared.migratable.SGXDocumentEntityRepository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface ProceduresRepository extends SGXAuditableEntityJPARepository<Procedure, Integer>, SGXDocumentEntityRepository<Procedure> {

	@Override
	@Transactional(readOnly = true)
	@Query("SELECT p " +
			"FROM DocumentProcedure dp " +
			"JOIN Procedure p ON (dp.pk.procedureId = p.id) " +
			"WHERE dp.pk.documentId IN :documentIds")
	List<Procedure> getEntitiesByDocuments(@Param("documentIds") List<Long> documentIds);

}
