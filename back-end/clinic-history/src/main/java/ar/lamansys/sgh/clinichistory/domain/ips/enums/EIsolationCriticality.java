package ar.lamansys.sgh.clinichistory.domain.ips.enums;

import ar.lamansys.sgx.shared.exceptions.NotFoundException;

import com.fasterxml.jackson.annotation.JsonCreator;

import lombok.Getter;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Getter
public enum EIsolationCriticality {
	LOW((short) 1, "Baja"),
	HIGH((short) 2, "Alta"),
	UNABLE_TO_ASSESS((short) 3, "Incapaz de evaluar")
	;
	//Please keep db table isolation_criticality up to date

	private final Short id;
	private final String description;

	EIsolationCriticality(Short id, String description) {
		this.id = id;
		this.description = description;
	}

	@JsonCreator
	public static List<EIsolationCriticality> getAll() {
		return Stream.of(EIsolationCriticality.values()).collect(Collectors.toList());
	}

	@JsonCreator
	public static EIsolationCriticality map(Short id) {
		for (EIsolationCriticality e : values()) {
			if (e.id.equals(id)) return e;
		}
		throw new NotFoundException("isolation-criticality-doesnt-exists", String.format("El tipo %s no existe", id));
	}
}
