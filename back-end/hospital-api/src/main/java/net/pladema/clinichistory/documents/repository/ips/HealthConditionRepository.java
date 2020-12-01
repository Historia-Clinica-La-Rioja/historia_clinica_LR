package net.pladema.clinichistory.documents.repository.ips;

import net.pladema.clinichistory.documents.repository.ips.entity.HealthCondition;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface HealthConditionRepository extends JpaRepository<HealthCondition, Integer> {

    @Transactional(readOnly = true)
    @Query("SELECT hc " +
            "FROM HealthCondition as hc " +
            "WHERE hc.id IN :ids ")
    List<HealthCondition> findByIds(@Param("ids") List<Integer> ids);
}
