package net.pladema.emergencycare.service.domain.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonFormat;
import ar.lamansys.sgx.shared.exceptions.NotFoundException;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum EEmergencyCareEntrance {

    CAMINANDO(1, "Caminando"),
    SILLA(2, "En silla de ruedas"),
    SINMEDICO(3, "Ambulancia sin médico"),
    CONMEDICO(4, "Ambulancia con médico");

    private final Short id;
    private final String description;

    EEmergencyCareEntrance(Number id, String description) {
        this.id = id.shortValue();
        this.description = description;
    }

    @JsonCreator
    public static List<EEmergencyCareEntrance> getAll(){
        return Stream.of(EEmergencyCareEntrance.values()).collect(Collectors.toList());
    }

    @JsonCreator
    public static EEmergencyCareEntrance getById(Short id){
        if (id == null)
            return null;
        for(EEmergencyCareEntrance ect : values()) {
            if(ect.id.equals(id)) return ect;
        }
        throw new NotFoundException("emergencyCareEntrance-not-exists", String.format("El valor %s es inválido", id));
    }

    public Short getId() {
        return id;
    }

    public String getDescription() {
        return description;
    }
}