package ar.lamansys.sgh.clinichistory.infrastructure.output.repository.ips;

import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.ips.entity.ObservationRiskFactor;
import ar.lamansys.sgx.shared.migratable.SGXDocumentEntityRepository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface ObservationRiskFactorRepository extends JpaRepository<ObservationRiskFactor, Integer>, SGXDocumentEntityRepository<ObservationRiskFactor> {

	@Override
	@Transactional(readOnly = true)
	@Query("SELECT orf " +
			"FROM DocumentRiskFactor drf " +
			"JOIN ObservationRiskFactor orf ON (drf.pk.observationRiskFactorId = orf.id) " +
			"WHERE drf.pk.documentId IN :documentIds")
	List<ObservationRiskFactor> getEntitiesByDocuments(@Param("documentIds") List<Long> documentIds);

}
