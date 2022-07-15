package ar.lamansys.sgh.clinichistory.infrastructure.output.repository.ips;

import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.ips.entity.indication.NursingRecord;
import ar.lamansys.sgx.shared.auditable.repository.SGXAuditableEntityJPARepository;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;


import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

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

	@Transactional(readOnly = true)
	@Query(value = "SELECT nr "
			+ "FROM NursingRecord nr "
			+ "WHERE nr.indicationId = :indicationId ")
	List<NursingRecord> getByIndicationId(@Param("indicationId") Integer indicationId);

	@Transactional
	@Modifying
	@Query( "UPDATE NursingRecord AS nr " +
			"SET nr.statusId = :statusId, " +
			"nr.updateable.updatedOn = CURRENT_TIMESTAMP, " +
			"nr.updateable.updatedBy = :userId, " +
			"nr.administrationTime = :administrationTime " +
			"WHERE nr.id = :nursingRecordId ")
	void updateStatus(@Param("nursingRecordId") Integer nursingRecordId,
					 @Param("statusId") short statusId,
					 @Param("userId") Integer userId,
					 @Param("administrationTime") LocalDateTime administrationTime);

	@Transactional(readOnly = true)
	@Query(value = "SELECT nr.indicationId " +
					"FROM NursingRecord nr " +
					"WHERE nr.id = :id ")
	Optional<Integer> getIndicationIdById (@Param("id") Integer id);

}
