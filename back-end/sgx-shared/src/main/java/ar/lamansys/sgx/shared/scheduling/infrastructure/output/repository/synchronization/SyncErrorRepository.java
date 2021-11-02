package ar.lamansys.sgx.shared.scheduling.infrastructure.output.repository.synchronization;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SyncErrorRepository extends JpaRepository<SyncError, Long> {

}
