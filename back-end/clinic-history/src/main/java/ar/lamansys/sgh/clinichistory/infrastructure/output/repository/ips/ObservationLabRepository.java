package ar.lamansys.sgh.clinichistory.infrastructure.output.repository.ips;

import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.ips.entity.ObservationLab;
import ar.lamansys.sgx.shared.migratable.SGXDocumentEntityRepository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface ObservationLabRepository extends JpaRepository<ObservationLab, Integer>, SGXDocumentEntityRepository<ObservationLab> {

	@Override
	@Transactional(readOnly = true)
	@Query("SELECT ol " +
			"FROM DocumentLab dl " +
			"JOIN ObservationLab ol ON dl.pk.observationLabId = ol.id " +
			"WHERE dl.pk.documentId IN :documentIds")
	List<ObservationLab> getEntitiesByDocuments(@Param("documentIds")List<Long> documentIds);

}
