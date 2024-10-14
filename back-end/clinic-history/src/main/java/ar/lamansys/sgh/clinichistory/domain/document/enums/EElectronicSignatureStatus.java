package ar.lamansys.sgh.clinichistory.domain.document.enums;

import ar.lamansys.sgx.shared.exceptions.NotFoundException;
import lombok.Getter;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Getter
public enum EElectronicSignatureStatus {

	PENDING(1, "Pendiente"),
	SIGNED(2, "Firmado"),
	REJECTED(3, "Rechazado"),
	OUTDATED(4, "Vencido");

	private Short id;

	private String description;

	EElectronicSignatureStatus(Integer id, String description) {
		this.id = id.shortValue();
		this.description = description;
	}

	public void setId(Short id) {
		this.id = id;
	}

	public static EElectronicSignatureStatus map(Short id) {
		for (EElectronicSignatureStatus e : values()) {
			if (e.id.equals(id)) return e;
		}
		throw new NotFoundException("status-not-exists", String.format("El estado %s no existe", id));
	}

	public static List<EElectronicSignatureStatus> getAll() {
		return Stream.of(EElectronicSignatureStatus.values()).collect(Collectors.toList());
	}

}
