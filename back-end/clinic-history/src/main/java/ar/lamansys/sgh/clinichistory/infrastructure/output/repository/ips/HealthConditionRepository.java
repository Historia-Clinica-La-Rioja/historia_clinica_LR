package ar.lamansys.sgh.clinichistory.infrastructure.output.repository.ips;

import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.ips.entity.HealthCondition;
import ar.lamansys.sgx.shared.auditable.repository.SGXAuditableEntityJPARepository;

import ar.lamansys.sgx.shared.migratable.SGXDocumentEntityRepository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface HealthConditionRepository extends SGXAuditableEntityJPARepository<HealthCondition, Integer> , SGXDocumentEntityRepository<HealthCondition> {

    @Transactional(readOnly = true)
    @Query("SELECT hc " +
            "FROM HealthCondition as hc " +
            "WHERE hc.id IN :ids ")
    List<HealthCondition> findByIds(@Param("ids") List<Integer> ids);

	@Override
	@Transactional(readOnly = true)
	@Query("SELECT hc " +
			"FROM DocumentHealthCondition dhc " +
			"JOIN HealthCondition hc ON dhc.pk.healthConditionId = hc.id " +
			"WHERE dhc.pk.documentId IN :documentIds")
	List<HealthCondition> getEntitiesByDocuments(@Param("documentIds") List<Long> documentIds);


}
