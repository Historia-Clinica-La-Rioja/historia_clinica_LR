package ar.lamansys.sgh.clinichistory.domain.ips.enums;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.fasterxml.jackson.annotation.JsonCreator;

import ar.lamansys.sgx.shared.exceptions.NotFoundException;
import lombok.Getter;

@Getter
public enum EIsolationType {
	//See https://browser.ihtsdotools.org/?perspective=full&conceptId1=40174006&edition=MAIN/2024-10-01&release=&languages=en&latestRedirect=false
	CONTACT((short) 1, "Contacto"),
	RESPIRATORY_SECRETION((short) 2, "Gotas"),
	AIRBORNE((short) 3, "Aereo (respiratorio)"),
	PREVENTIVE((short) 4, "Aislamiento protector"),
	ENTOMOLOGICAL((short) 5, "Aislamiento entomológico"),
	OTHER((short) 6, "Otros");


	//Please keep db table isolation_type up to date

	private final Short id;
	private final String description;

	EIsolationType(Short id, String description) {
		this.id = id;
		this.description = description;
	}

	@JsonCreator
	public static List<EIsolationType> getAll() {
		return Stream.of(EIsolationType.values()).collect(Collectors.toList());
	}

	@JsonCreator
	public static EIsolationType map(Short id) {
		for (EIsolationType e : values()) {
			if (e.id.equals(id)) return e;
		}
		throw new NotFoundException("isolation-type-not-exists", String.format("El tipo %s no existe", id));
	}
}

