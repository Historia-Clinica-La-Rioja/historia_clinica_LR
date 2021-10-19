package ar.lamansys.sgh.publicapi.infrastructure.output;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VAttentionRepository extends JpaRepository<VAttention, Integer> {

}
