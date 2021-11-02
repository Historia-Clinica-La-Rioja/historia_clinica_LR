package ar.lamansys.sgx.shared.scheduling.infrastructure.output.rest;

import org.springframework.http.HttpStatus;

import java.util.List;
import java.util.stream.Collectors;

public class TAWSException extends Exception {

	private static final long serialVersionUID = 8805751514318626691L;

	public final HttpStatus httpStatus;
	public final List<TAWSResponseError> error;

	public TAWSException(HttpStatus httpStatus, List<TAWSResponseError> error) {
		this.httpStatus = httpStatus;
		this.error = error;
	}

	@Override
	public String getMessage() {
		final StringBuilder sb = new StringBuilder("StatusCode: ").append(httpStatus.toString());
		if (!error.isEmpty()) {
			sb.append("\n Errors:[ \n");
			sb.append(
					error.stream()
							.map(TAWSResponseError::toString)
							.collect(Collectors.joining("\n")));
			sb.append("]");
		}
		return sb.toString();
	}
}
