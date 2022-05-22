package ar.lamansys.sgh.clinichistory.domain.ips;

import ar.lamansys.sgx.shared.exceptions.NotFoundException;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum EUnit {

	MG(1, "mg"),
	ML(2, "ml");

	private final Short id;
	private final String description;

	EUnit(Number id, String description) {
		this.id = id.shortValue();
		this.description = description;
	}

	@JsonCreator
	public static List<EUnit> getAll(){
		return Stream.of(EUnit.values()).collect(Collectors.toList());
	}

	@JsonCreator
	public static EUnit getById(Short id){
		if (id == null)
			return null;
		for(EUnit u: values()) {
			if(u.id.equals(id)) return u;
		}
		throw new NotFoundException("UnitType-not-exists", String.format("El valor %s es inválido", id));
	}

	public Short getId() {
		return id;
	}

	public String getDescription() {
		return description;
	}
}