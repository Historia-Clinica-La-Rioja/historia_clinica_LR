package ar.lamansys.sgh.clinichistory.domain.ips;

import ar.lamansys.sgx.shared.exceptions.NotFoundException;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum EVia {

	PERIFERICA(1, "Periférica"),
	SUBCUTANEA(2, "Subcutánea"),
	CENTRAL(3, "Central");

	private final Short id;
	private final String description;

	EVia(Number id, String description) {
		this.id = id.shortValue();
		this.description = description;
	}

	@JsonCreator
	public static List<EVia> getAll(){
		return Stream.of(EVia.values()).collect(Collectors.toList());
	}

	@JsonCreator
	public static EVia getById(Short id){
		if (id == null)
			return null;
		for(EVia v: values()) {
			if(v.id.equals(id)) return v;
		}
		throw new NotFoundException("ViaType-not-exists", String.format("El valor %s es inválido", id));
	}

	public Short getId() {
		return id;
	}

	public String getDescription() {
		return description;
	}
}