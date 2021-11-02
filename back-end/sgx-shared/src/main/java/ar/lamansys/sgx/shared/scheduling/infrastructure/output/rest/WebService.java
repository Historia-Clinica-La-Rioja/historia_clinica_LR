package ar.lamansys.sgx.shared.scheduling.infrastructure.output.rest;

public interface WebService<Y, C> {
	C createOrUpdate(Y payload) throws TAWSException;

	TAWSDeleteResponse delete(String id) throws TAWSException;

	boolean isMocked();

}

