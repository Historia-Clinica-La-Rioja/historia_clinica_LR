package ar.lamansys.sgx.shared.scheduling.infrastructure.output.service;

public interface AbstractSync<I> {

	I getId();

	void updateErrorStatus(Integer statusCode);
	void updateOkStatus(String externalId);

	String getExternalId();

	default boolean alreadyExists() {
		return getExternalId() != null;
	}
}
