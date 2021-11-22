package ar.lamansys.sgh.publicapi.infrastructure.output;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AttentionReadsRepository extends JpaRepository<AttentionReads, AttentionReadsPK> {

    @Query(value = "SELECT ar FROM AttentionReads ar WHERE ar.attentionReadsPK.attentionId = :attentionId")
    List<AttentionReads> findByAttentionId(@Param("attentionId") Long attentionId);
}
