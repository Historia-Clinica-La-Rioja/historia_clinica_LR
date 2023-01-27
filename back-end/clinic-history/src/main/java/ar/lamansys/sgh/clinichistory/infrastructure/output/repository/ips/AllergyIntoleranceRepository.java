package ar.lamansys.sgh.clinichistory.infrastructure.output.repository.ips;

import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.ips.entity.AllergyIntolerance;
import ar.lamansys.sgx.shared.auditable.repository.SGXAuditableEntityJPARepository;
import ar.lamansys.sgx.shared.migratable.SGXDocumentEntityRepository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface AllergyIntoleranceRepository extends SGXAuditableEntityJPARepository<AllergyIntolerance, Integer>, SGXDocumentEntityRepository<AllergyIntolerance> {

	@Override
	@Transactional(readOnly = true)
	@Query("SELECT ai " +
			"FROM DocumentAllergyIntolerance dai " +
			"JOIN AllergyIntolerance ai ON dai.pk.allergyIntoleranceId = ai.id " +
			"WHERE dai.pk.documentId IN :documentIds")
	List<AllergyIntolerance> getEntitiesByDocuments(@Param("documentIds") List<Long> documentIds);
}
