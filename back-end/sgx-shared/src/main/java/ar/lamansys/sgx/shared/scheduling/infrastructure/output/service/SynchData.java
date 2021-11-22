package ar.lamansys.sgx.shared.scheduling.infrastructure.output.service;


import ar.lamansys.sgx.shared.scheduling.infrastructure.output.repository.synchronization.AbstractData;

import java.util.function.Function;
import java.util.function.Supplier;

public class SynchData<D, S extends AbstractSync<I>, I extends Number> {
	private final SynchAction action;
	public final D data;
	public final S sync;

	private SynchData(SynchAction action, D data, S sync) {
		this.action = action;
		this.data = data;
		this.sync = sync;
	}

	public boolean shouldSendDelete() {
		return action == SynchAction.DELETE;
	}

	public static <D, S extends AbstractSync<I>, I extends Number> SynchData<D,S,I> delete(S sync) {
		return new SynchData<>(SynchAction.DELETE, null, sync);
	}

	public static <D, S extends AbstractSync<I>, I extends Number> SynchData<D,S,I> send(D data, S sync) {
		SynchAction action = sync.alreadyExists()? SynchAction.UPDATE : SynchAction.CREATE;
		return new SynchData<>(action, data, sync);
	}

	public static <D extends AbstractData<I>, S extends AbstractSync<I>, I extends Number> Supplier<SynchData<D,S,I>> create(D data, Function<I, S> createSynch) {
		return () -> new SynchData<>(SynchAction.CREATE, data, createSynch.apply(data.getId()));
	}

	public static <D, S extends AbstractSync<I>, I extends Number> Supplier<SynchData<D,S,I>> skip(S sync) {
		return () -> new SynchData<>(SynchAction.SKIP, null, sync);
	}

	public static <D, S extends AbstractSync<I>, I extends Number> Function<D, SynchData<D,S,I>> send(S sync) {
		return (data) -> SynchData.send(data, sync);
	}

	public static <D, S extends AbstractSync<I>, I extends Number> Function<S, SynchData<D,S,I>> send(D data) {
		return (sync) -> SynchData.send(data, sync);
	}

	@Override
	public String toString() {
		final StringBuilder sb = new StringBuilder("SynchData{");
		sb.append("action=").append(action);
		sb.append(", data=").append(data);
		sb.append('}');
		return sb.toString();
	}
}