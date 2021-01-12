package net.pladema.emergencycare.service.domain.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonFormat;
import net.pladema.sgx.exceptions.NotFoundException;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum EEmergencyCareEntrance {

    CAMINANDO(1, "Caminando"),
    SILLA(2, "En silla de ruedas"),
    CONMEDICO(3, "Ambulancia con médico"),
    SINMEDICO(4, "Ambulancia sin médico");

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