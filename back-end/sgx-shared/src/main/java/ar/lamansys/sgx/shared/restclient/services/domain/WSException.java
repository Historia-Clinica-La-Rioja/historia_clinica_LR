package ar.lamansys.sgx.shared.restclient.services.domain;

import org.springframework.http.HttpStatus;

import java.util.List;
import java.util.stream.Collectors;

public class WSException extends Exception {

	private static final long serialVersionUID = 8805751514318626691L;

	private final HttpStatus httpStatus;
	private final List<WSResponseError> error;

	public WSException(HttpStatus httpStatus, List<WSResponseError> error) {
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
							.map(WSResponseError::toString)
							.collect(Collectors.joining("\n")));
			sb.append("]");
		}
		return sb.toString();
	}
}
