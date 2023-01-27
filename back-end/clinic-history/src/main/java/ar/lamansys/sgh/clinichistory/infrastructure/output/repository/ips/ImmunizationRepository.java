package ar.lamansys.sgh.clinichistory.infrastructure.output.repository.ips;

import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.ips.entity.Inmunization;
import ar.lamansys.sgx.shared.auditable.repository.SGXAuditableEntityJPARepository;
import ar.lamansys.sgx.shared.migratable.SGXDocumentEntityRepository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface ImmunizationRepository extends SGXAuditableEntityJPARepository<Inmunization, Integer>, SGXDocumentEntityRepository<Inmunization> {

	@Override
	@Transactional(readOnly = true)
	@Query("SELECT i " +
			"FROM DocumentInmunization di " +
			"JOIN Inmunization i ON di.pk.inmunizationId = i.id " +
			"WHERE di.pk.documentId IN :documentIds")
	List<Inmunization> getEntitiesByDocuments(@Param("documentIds") List<Long> documentIds);

}
