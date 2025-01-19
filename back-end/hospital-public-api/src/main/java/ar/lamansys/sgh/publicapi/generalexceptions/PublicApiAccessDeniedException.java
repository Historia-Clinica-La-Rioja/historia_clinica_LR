package ar.lamansys.sgh.publicapi.generalexceptions;


public class PublicApiAccessDeniedException extends RuntimeException {
	public final String group;
	public final String endpoint;

	// solo las subclases pueden usarlo
	protected PublicApiAccessDeniedException(String group, String endpoint) {
		this.group = group;
		this.endpoint = endpoint;
	}
}
