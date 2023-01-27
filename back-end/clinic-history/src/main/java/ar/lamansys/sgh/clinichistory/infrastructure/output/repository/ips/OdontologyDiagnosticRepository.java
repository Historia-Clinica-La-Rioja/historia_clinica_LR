package ar.lamansys.sgh.clinichistory.infrastructure.output.repository.ips;

import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.ips.entity.OdontologyDiagnostic;
import ar.lamansys.sgx.shared.auditable.repository.SGXAuditableEntityJPARepository;
import ar.lamansys.sgx.shared.migratable.SGXDocumentEntityRepository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface OdontologyDiagnosticRepository extends SGXAuditableEntityJPARepository<OdontologyDiagnostic, Integer>, SGXDocumentEntityRepository<OdontologyDiagnostic> {

	@Override
	@Transactional(readOnly = true)
	@Query("SELECT od " +
			"FROM DocumentOdontologyDiagnostic dod " +
			"JOIN OdontologyDiagnostic od ON dod.pk.odontologyDiagnosticId = od.id " +
			"WHERE dod.pk.documentId IN :documentIds")
	List<OdontologyDiagnostic> getEntitiesByDocuments(@Param("documentIds") List<Long> documentIds);

}
