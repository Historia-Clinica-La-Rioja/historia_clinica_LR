package ar.lamansys.sgx.shared.scheduling.infrastructure.output.repository.synchronization;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

import java.util.Optional;

public interface AbstractDataRepository<D extends AbstractData<I>, I> {

	Optional<D> findById(I id);
	Slice<D> getDataToSynch(Pageable pageable);
}
