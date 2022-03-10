package ar.lamansys.sgx.shared.actuator.infrastructure.output.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface SystemPropertyRepository extends JpaRepository<SystemProperty, Integer> {


    @Transactional
    @Modifying
    @Query("DELETE FROM SystemProperty sp WHERE sp.nodeId = :nodeId ")
    void deleteByIpNodeId(@Param("nodeId") String nodeId);
}
