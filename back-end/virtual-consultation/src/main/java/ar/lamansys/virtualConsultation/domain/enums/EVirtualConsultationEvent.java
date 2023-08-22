package ar.lamansys.virtualConsultation.domain.enums;

import ar.lamansys.sgx.shared.exceptions.NotFoundException;
import lombok.Getter;

@Getter
public enum EVirtualConsultationEvent {

	INCOMING_CALL(1, "LLAMADA ENTRANTE", "virtual-consultation-call-request"),
	CALL_CANCELED(2, "LLAMADA CANCELADA", "virtual-consultation-call-response"),
	CALL_REJECTED(3, "LLAMADA RECHAZADA", "virtual-consultation-call-response"),
	CALL_ACCEPTED(4, "LLAMADA ACEPTADA", "virtual-consultation-call-response");

	private Short id;
	private String value;
	private String wsPath;

	EVirtualConsultationEvent(Integer id, String value, String wsPath) {
		this.id = id.shortValue();
		this.value = value;
		this.wsPath = wsPath;
	}

	public void setId(Short id) {
		this.id = id;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public void setWsPath(String wsPath) {
		this.wsPath = wsPath;
	}

	public static EVirtualConsultationEvent map(Short id) {
		for (EVirtualConsultationEvent e : values()) {
			if (e.id.equals(id)) return e;
		}
		throw new NotFoundException("virtual-consultation-event-not-exists", String.format("El evento %s no existe", id));
	}


}
