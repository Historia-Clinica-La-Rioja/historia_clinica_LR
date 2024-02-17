package ar.lamansys.sgh.clinichistory.infrastructure.output.repository.masterdata.entity;

import ar.lamansys.sgx.shared.exceptions.NotFoundException;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum EProblemErrorReason {

    PROBLEM_INCORRECT((short) 1, "Problema incorrecto"),
    PATIENT_INCORRECT((short) 2, "Paciente asignado incorrectamente"),
    ;

    private final Short id;
    private final String description;

    EProblemErrorReason(Short id, String description) {
        this.id = id;
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public Number getId() {
        return id;
    }

    @JsonCreator
    public static List<EProblemErrorReason> getAll() {
        return Stream.of(EProblemErrorReason.values()).collect(Collectors.toList());
    }

    @JsonCreator
    public static EProblemErrorReason map(Short id) {
        for (EProblemErrorReason e : values()) {
            if (e.id.equals(id)) return e;
        }
        throw new NotFoundException("problem-error-reason-not-exists", String.format("El motivo %s no existe", id));
    }

}
