package ar.lamansys.sgh.clinichistory.infrastructure.output.repository.ips;


import ar.lamansys.sgx.shared.migratable.SGXDocumentEntityRepository;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.ips.entity.indication.Indication;
import ar.lamansys.sgx.shared.auditable.repository.SGXAuditableEntityJPARepository;

import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
public interface IndicationRepository extends SGXAuditableEntityJPARepository<Indication, Integer>, SGXDocumentEntityRepository<Indication> {

	@Transactional(readOnly=true)
	@Query(value="SELECT i.typeId "
			+ "FROM Indication i "
			+ "WHERE i.id = :indicationId")
	Optional<Short> getTypeById(@Param("indicationId")Integer indicationId);

	@Transactional
	@Modifying
	@Query( "UPDATE Indication AS i " +
			"SET i.statusId = :statusId, " +
			"i.updateable.updatedOn = CURRENT_TIMESTAMP, " +
			"i.updateable.updatedBy = :userId " +
			"WHERE i.id = :indicationId ")
	void updateStatus(@Param("indicationId") Integer indicationId,
					  @Param("statusId") short statusId,
					  @Param("userId") Integer userId);

	@Override
	@Transactional(readOnly = true)
	@Query(value = "SELECT i "
			+ "FROM DocumentIndication di "
			+ "JOIN Indication i ON di.pk.indicationId = i.id "
			+ "WHERE di.pk.documentId IN :documentIds")
	List<Indication> getEntitiesByDocuments(@Param("documentIds") List<Long> documentIds);


}
