package ar.lamansys.sgx.shared.scheduling.infrastructure.output.rest;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class TAWSDeleteResponse {
	public final int n;
	public final int ok;

	@JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
	public TAWSDeleteResponse(@JsonProperty("n") int n, @JsonProperty("ok") int ok) {
		this.n = n;
		this.ok = ok;
	}

	@Override
	public String toString() {
		final StringBuilder sb = new StringBuilder("DistritoDeleteResponse{");
		sb.append("n=").append(n);
		sb.append(", ok=").append(ok);
		sb.append('}');
		return sb.toString();
	}
}
