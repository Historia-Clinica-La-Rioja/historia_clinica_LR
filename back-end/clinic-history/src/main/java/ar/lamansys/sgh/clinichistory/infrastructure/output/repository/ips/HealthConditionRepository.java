package ar.lamansys.sgh.clinichistory.infrastructure.output.repository.ips;

import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.ips.entity.HealthCondition;
import ar.lamansys.sgx.shared.auditable.repository.SGXAuditableEntityJPARepository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface HealthConditionRepository extends SGXAuditableEntityJPARepository<HealthCondition, Integer> {

    @Transactional(readOnly = true)
    @Query("SELECT hc " +
            "FROM HealthCondition as hc " +
            "WHERE hc.id IN :ids ")
    List<HealthCondition> findByIds(@Param("ids") List<Integer> ids);
}
