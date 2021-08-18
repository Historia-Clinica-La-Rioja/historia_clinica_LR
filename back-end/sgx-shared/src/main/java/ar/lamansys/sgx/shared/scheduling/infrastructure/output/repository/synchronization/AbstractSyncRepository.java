package ar.lamansys.sgx.shared.scheduling.infrastructure.output.repository.synchronization;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

import java.util.Optional;

public interface AbstractSyncRepository<S, I> {
	
	Slice<S> getDeletedElements(Pageable pageable);
	Optional<S> findById(I id);
	S saveAndFlush(S entity);
	Slice<S> getPriorityElement(Pageable pageable);
}
