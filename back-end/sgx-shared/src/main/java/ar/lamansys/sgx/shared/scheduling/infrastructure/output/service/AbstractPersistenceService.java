package ar.lamansys.sgx.shared.scheduling.infrastructure.output.service;


import ar.lamansys.sgx.shared.scheduling.infrastructure.output.repository.synchronization.AbstractData;
import ar.lamansys.sgx.shared.scheduling.infrastructure.output.repository.synchronization.AbstractDataRepository;
import ar.lamansys.sgx.shared.scheduling.infrastructure.output.repository.synchronization.AbstractSyncRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Stream;

public class AbstractPersistenceService<D extends AbstractData<I>, S extends AbstractSync<I>, I extends Number> implements PersistenceService<D, S, I> {

	@Value("${scheduling.send-deleted:false }")
	private boolean sendDeleted;

	private static final int PAGE = 0;
	private static final int PAGE_SIZE = 1;
	
	private final SyncErrorService<S, I> syncErrorService;
	private final AbstractDataRepository<D, I> dataRepository;
	private final AbstractSyncRepository<S, I> syncRepository;
	private final Function<I, S> createSynch;

	public AbstractPersistenceService(SyncErrorService<S, I> syncErrorService,
									  AbstractDataRepository<D, I> dataRepository,
									  AbstractSyncRepository<S, I> syncRepository,
									  Function<I, S> createSynch) {
		this.syncErrorService = syncErrorService;
		this.dataRepository = dataRepository;
		this.syncRepository = syncRepository;
		this.createSynch = createSynch;
	}

	@Override
	@Transactional(readOnly = true)
	public Optional<SynchData<D, S, I>> firstEntityToSend() {
		Stream<Optional<SynchData<D, S, I>>> entities = sendDeleted
				? Stream.of(firstEntityToDelete(), firstEntityByPriority(), firstEntityToUpdate())
				: Stream.of(firstEntityByPriority(), firstEntityToUpdate());
		return entities.filter(Optional::isPresent)
				.map(Optional::get)
				.findFirst();
	}

	@Override
	public Optional<SynchData<D, S, I>> firstEntityToDelete() {
		return syncRepository.getDeletedElements(buildPageable())
				.get()
				.findFirst()
				.map(SynchData::delete);
	}

	@Override
	public Optional<SynchData<D, S, I>> firstEntityByPriority() {
		return syncRepository.getPriorityElement(buildPageable())
				.get()
				.findFirst()
				.map(this::fromSync);

	}

	@Override
	public Optional<SynchData<D, S, I>> firstEntityToUpdate() {
		return dataRepository.getDataToSynch(buildPageable())
				.get()
				.findFirst()
				.map(this::fromData);
	}

	@Override
	public SynchData<D, S, I> fromSync(S sync) {
		return dataRepository.findById(sync.getId())
				.map(SynchData.send(sync))
				// esto no debería pasar, pero si pasa enviamos un DELETE
				.orElseGet(SynchData.skip(sync));
	}

	@Override
	public SynchData<D, S, I> fromData(D data) {
		return syncRepository.findById(data.getId())
				.map(SynchData.send(data))
				.orElseGet(SynchData.create(data, createSynch));
	}

	@Override
	public void markEntityAsSent(S entitySync, String externalId) {
		entitySync.updateOkStatus(externalId);

		syncRepository.saveAndFlush(entitySync);
	}

	@Override
	public void markEntityWithError(S entitySync, HttpStatus httpStatus, String error, ESyncError entity) {
		entitySync.updateErrorStatus(httpStatus.value());

		syncRepository.saveAndFlush(entitySync);
		syncErrorService.createError(entitySync, entity, error);
	}

	@Override
	public int getPage() {
		return PAGE;
	}

	@Override
	public int getPageSize() {
		return PAGE_SIZE;
	}

}
