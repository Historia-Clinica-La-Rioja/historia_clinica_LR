package ar.lamansys.sgx.shared.scheduling.infrastructure.output.service;


import ar.lamansys.sgx.shared.scheduling.infrastructure.output.repository.synchronization.SyncError;
import ar.lamansys.sgx.shared.scheduling.infrastructure.output.repository.synchronization.SyncErrorRepository;
import org.springframework.stereotype.Service;

@Service
public class SyncErrorServiceImpl<S extends AbstractSync<I>, I extends Number> implements SyncErrorService<S, I> {

	private SyncErrorRepository syncErrorRepository;
	
	public SyncErrorServiceImpl(SyncErrorRepository syncErrorRepository) {
		this.syncErrorRepository = syncErrorRepository;
	}

	@Override
	public void createError(S sync, ESyncError entity, String log) {
		SyncError error = new SyncError(sync.getId().longValue(), entity.getValue(), log);
		saveAndFlush(error);
	}
	
	private void saveAndFlush(SyncError error) {
		syncErrorRepository.save(error);
	}

}
