package ar.lamansys.refcounterref.domain.enums;

import com.fasterxml.jackson.annotation.JsonFormat;

import ar.lamansys.sgx.shared.exceptions.NotFoundException;
import com.fasterxml.jackson.annotation.JsonCreator;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum EReferenceCounterReferenceType {

    REFERENCIA(1, "Referencia"),
    CONTRARREFERENCIA(2, "Contrarreferencia");

    private final Short id;
    private final String description;

    EReferenceCounterReferenceType(Number id, String description) {
        this.id = id.shortValue();
        this.description = description;
    }

    @JsonCreator
    public static List<EReferenceCounterReferenceType> getAll(){
        return Stream.of(EReferenceCounterReferenceType.values()).collect(Collectors.toList());
    }

    @JsonCreator
    public static EReferenceCounterReferenceType getById(Short id){
        if (id == null)
            return null;
        for(EReferenceCounterReferenceType rcrt: values()) {
            if(rcrt.id.equals(id)) return rcrt;
        }
        throw new NotFoundException("referenceCounterReferenceType-not-exists", String.format("El valor %s es inválido", id));
    }

    public Short getId() {
        return id;
    }

    public String getDescription() {
        return description;
    }
}