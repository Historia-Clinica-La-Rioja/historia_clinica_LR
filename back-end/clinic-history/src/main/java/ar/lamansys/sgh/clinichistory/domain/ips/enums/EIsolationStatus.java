package ar.lamansys.sgh.clinichistory.domain.ips.enums;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.fasterxml.jackson.annotation.JsonCreator;

import ar.lamansys.sgx.shared.exceptions.NotFoundException;
import lombok.Getter;

@Getter
public enum EIsolationStatus {
	ONGOING((short) 1, "Vigente"),
	EXPIRED((short) 2, "Finalizada"),
	CANCELLED((short) 3, "Finalizada");
	//Please keep db table isolation_status up to date

	private final Short id;
	private final String description;

	EIsolationStatus(Short id, String description) {
		this.id = id;
		this.description = description;
	}

	@JsonCreator
	public static List<EIsolationStatus> getAll() {
		return Stream.of(EIsolationStatus.values()).collect(Collectors.toList());
	}

	@JsonCreator
	public static EIsolationStatus map(Short id) {
		for (EIsolationStatus e : values()) {
			if (e.id.equals(id)) return e;
		}
		throw new NotFoundException("isolation-status-doesnt-exists", String.format("El estado %s no existe", id));
	}

	public boolean isOngoing() {
		return ONGOING.equals(this);
	}

	public boolean isExpired() {return EXPIRED.equals(this);}
	public boolean isCancelled() {return CANCELLED.equals(this);}
}

