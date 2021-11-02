package net.pladema.sgx.backoffice.rest.dto;

public class BackofficeDeleteResponse<I> {
	public final I id;

	public BackofficeDeleteResponse(I id) {
		this.id = id;
	}
}
