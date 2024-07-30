package net.pladema.emergencycare.service.domain.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonFormat;
import ar.lamansys.sgx.shared.exceptions.NotFoundException;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum EEmergencyCareState {

    ATENCION(1, "En atención"),
    ESPERA(2, "En espera"),
    ALTA_ADMINISTRATIVA(3, "Con alta administrativa"),
    ALTA_MEDICA(4, "Con alta médica"),
	AUSENTE(5, "Ausente"),
	LLAMADO(6, "Llamado");

    private final Short id;
    private final String description;

	/**
	 * Matriz que indica las transiciones de estado válidas.
	 * El nro de fila representa el from.
	 * El nro de columna representa el to
	 */
	private static final boolean[][] validTransitionMatrix = {
			/*  ->  |  0  |  1   |  2   |  3   |  4   |  5   |  6   | */
			/* 0  */{false, false, false, false, false, false, false},
			/* 1  */{false, false, true , false, true , false, false},
			/* 2  */{false, true , false, true , false, true , true },
			/* 3  */{false, false, false, false, false, false, false},
			/* 4  */{false, false, false, true , false, false, false},
			/* 5  */{false, true , true , true , false, false, true },
			/* 6  */{false, true , true , true , true , true , true }
	};

    EEmergencyCareState(Number id, String description) {
        this.id = id.shortValue();
        this.description = description;
    }

    @JsonCreator
    public static List<EEmergencyCareState> getAll(){
        return Stream.of(EEmergencyCareState.values()).collect(Collectors.toList());
    }

    @JsonCreator
    public static EEmergencyCareState getById(Short id){
        if (id == null)
            return null;
        for(EEmergencyCareState ecs: values()) {
            if(ecs.id.equals(id)) return ecs;
        }
        throw new NotFoundException("emergencyCareState-not-exists", String.format("El valor %s es inválido", id));
    }

    public Short getId() {
        return id;
    }

    public String getDescription() {
        return description;
    }

	public static boolean validTransition(EEmergencyCareState from, EEmergencyCareState to){
		return validTransitionMatrix[from.getId()][to.getId()];
	}

	public static List<Short> getAllForEmergencyCareList(){
		return Stream.of(EEmergencyCareState.ATENCION, EEmergencyCareState.ESPERA, EEmergencyCareState.ALTA_MEDICA,
						EEmergencyCareState.AUSENTE, EEmergencyCareState.LLAMADO)
				.map(EEmergencyCareState::getId)
				.collect(Collectors.toList());
	}

	public static List<Short> getAllValidForCreateTriage(){
		return Stream.of(EEmergencyCareState.ATENCION, EEmergencyCareState.ESPERA, EEmergencyCareState.ALTA_MEDICA, EEmergencyCareState.AUSENTE)
				.map(EEmergencyCareState::getId)
				.collect(Collectors.toList());
	}
}
