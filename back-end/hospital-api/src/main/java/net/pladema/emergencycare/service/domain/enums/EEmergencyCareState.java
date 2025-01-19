package net.pladema.emergencycare.service.domain.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonFormat;
import ar.lamansys.sgx.shared.exceptions.NotFoundException;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum EEmergencyCareState {

    ATENCION(1, "En atención", (short) 1),
    ESPERA(2, "En espera de atención", (short) 3),
    ALTA_ADMINISTRATIVA(3, "Con alta administrativa", (short) 6),
    ALTA_PACIENTE(4, "Alta de paciente", (short) 5),
	AUSENTE(5, "Ausente", (short) 4),
	LLAMADO(6, "Llamado", (short) 2);

    private final Short id;
    private final String description;

	private final Short order;

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

    EEmergencyCareState(Number id, String description, Short order) {
        this.id = id.shortValue();
        this.description = description;
		this.order = order;
    }

    @JsonCreator
    public static List<EEmergencyCareState> getAll(){
        return Stream.of(EEmergencyCareState.values())
				.sorted(Comparator.comparing(ecs -> ecs.order))
				.collect(Collectors.toList());
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
		return Stream.of(EEmergencyCareState.ATENCION, EEmergencyCareState.ESPERA, EEmergencyCareState.ALTA_PACIENTE,
						EEmergencyCareState.AUSENTE, EEmergencyCareState.LLAMADO)
				.map(EEmergencyCareState::getId)
				.collect(Collectors.toList());
	}

	public static List<Short> getAllValidForCreateTriage(){
		return Stream.of(EEmergencyCareState.ATENCION, EEmergencyCareState.ESPERA, EEmergencyCareState.AUSENTE,
						EEmergencyCareState.LLAMADO)
				.map(EEmergencyCareState::getId)
				.collect(Collectors.toList());
	}
}
