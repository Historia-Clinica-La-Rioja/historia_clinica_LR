package ar.lamansys.sgh.clinichistory.infrastructure.output.repository.ips;

import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.ips.entity.indication.NursingRecord;
import ar.lamansys.sgx.shared.auditable.repository.SGXAuditableEntityJPARepository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;


import java.util.List;

@Repository
public interface NursingRecordRepository extends SGXAuditableEntityJPARepository<NursingRecord, Integer> {

	@Transactional(readOnly = true)
	@Query(value = "SELECT nr "
			+ "FROM NursingRecord nr "
			+ "JOIN Indication i ON (nr.indicationId = i.id) "
			+ "JOIN DocumentIndication di ON di.pk.indicationId = i.id "
			+ "JOIN Document doc ON di.pk.documentId = doc.id "
			+ "WHERE doc.sourceId = :internmentEpisodeId "
			+ "ORDER BY nr.indicationId ")
	List<NursingRecord> getByInternmentEpisodeId(@Param("internmentEpisodeId") Integer intermentEpisodeId);

}
