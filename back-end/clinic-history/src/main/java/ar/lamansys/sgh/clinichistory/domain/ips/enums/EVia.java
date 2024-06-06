package ar.lamansys.sgh.clinichistory.domain.ips.enums;

import ar.lamansys.sgx.shared.exceptions.NotFoundException;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import lombok.Getter;

@Getter
@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum EVia {

	PERIFERICA(1, "Periférica"),
	SUBCUTANEA(2, "Subcutánea"),
	CENTRAL(3, "Central"),
	ORAL(4,"Oral"),
	INHALATION(5,"Inhalatoria"),
	INTRAVENOUS(6,"Endovenosa"),
	OTHER(7, "Otra"),
	EPIDURAL(8, "Epidural"),
	RECTAL(9, "Rectal"),
	TOPICAL(10, "Tópica"),
	SUBARACHNOID(11, "Subaracnoidea (Raquídea)"),
	;


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
	public static List<EVia> getAllPharmaco(){
		return Stream.of(EVia.PERIFERICA, EVia.SUBCUTANEA, EVia.CENTRAL, EVia.ORAL, EVia.INHALATION).collect(Collectors.toList());
	}

	@JsonCreator
	public static List<EVia> getAllParenteral(){
		return Stream.of(EVia.PERIFERICA,EVia.SUBCUTANEA,EVia.CENTRAL).collect(Collectors.toList());
	}

	@JsonCreator
	public static List<EVia> getPreMedication(){
		return Stream.of(EVia.INTRAVENOUS, EVia.SUBCUTANEA, EVia.INHALATION, EVia.OTHER).collect(Collectors.toList());
	}

	@JsonCreator
	public static List<EVia> getAnestheticPlan(){
		return Stream.of(EVia.INTRAVENOUS, EVia.EPIDURAL, EVia.INHALATION, EVia.RECTAL, EVia.TOPICAL, EVia.SUBARACHNOID, EVia.OTHER).collect(Collectors.toList());
	}

	@JsonCreator
	public static List<EVia> getAnestheticAgent(){
		return Stream.of(EVia.INTRAVENOUS, EVia.EPIDURAL, EVia.INHALATION, EVia.RECTAL, EVia.TOPICAL, EVia.SUBARACHNOID, EVia.OTHER).collect(Collectors.toList());
	}

	@JsonCreator
	public static List<EVia> getNonAnestheticDrug(){
		return Stream.of(EVia.INTRAVENOUS, EVia.SUBCUTANEA, EVia.INHALATION, EVia.OTHER).collect(Collectors.toList());
	}

	@JsonCreator
	public static List<EVia> getAntibioticProphylaxis(){
		return Stream.of(EVia.INTRAVENOUS, EVia.OTHER).collect(Collectors.toList());
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

}