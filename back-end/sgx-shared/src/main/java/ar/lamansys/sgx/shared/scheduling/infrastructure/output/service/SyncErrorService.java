package ar.lamansys.sgx.shared.scheduling.infrastructure.output.service;


public interface SyncErrorService<S extends AbstractSync<I>, I extends Number> {
	void createError(S sync, ESyncError entity, String log);
}
