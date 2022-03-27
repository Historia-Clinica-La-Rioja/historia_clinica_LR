package ar.lamansys.sgh.shared.infrastructure.input.service.events.exceptions;

public class NewPublisherException extends RuntimeException {

	public NewPublisherException(String publisherName) {
		super("Ya se encuentra registrado un publicador con el nombre: " + publisherName);
	}
}
