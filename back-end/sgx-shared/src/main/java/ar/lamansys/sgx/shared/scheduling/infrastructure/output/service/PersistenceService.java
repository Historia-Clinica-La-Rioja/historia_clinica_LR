package ar.lamansys.sgx.shared.scheduling.infrastructure.output.service;

import ar.lamansys.sgx.shared.scheduling.infrastructure.output.repository.synchronization.AbstractData;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

public interface PersistenceService<D extends AbstractData<I>, S extends AbstractSync<I>, I extends Number> {

    @Transactional(readOnly = true)
    Optional<SynchData<D, S, I>> firstEntityToSend();

    Optional<SynchData<D, S, I>> firstEntityToDelete();

    Optional<SynchData<D, S, I>> firstEntityByPriority();

    Optional<SynchData<D, S, I>> firstEntityToUpdate();

    SynchData<D, S, I> fromSync(S sync);

    SynchData<D, S, I> fromData(D data);

    void markEntityAsSent(S entitySync, String externalId);

    void markEntityWithError(S entitySync, HttpStatus httpStatus, String error, ESyncError entity);

    default Pageable buildPageable() {
        return PageRequest.of(getPage(), getPageSize(), Sort.unsorted());
    }

    int getPage();

    int getPageSize();
}
