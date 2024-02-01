package ar.lamansys.sgh.clinichistory.domain.ips.enums;

import ar.lamansys.sgx.shared.exceptions.NotFoundException;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonFormat;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import lombok.Getter;

@JsonFormat(shape = JsonFormat.Shape.OBJECT)
@Getter
public enum EAnestheticTechnique {

    INHALATION((short) 1,"Inhalatoria"),
    INTRAVENOUS((short) 2,"Endovenosa"),
    BOTH((short) 3,"Ambas"),
    ;

    private final Short id;
    private final String description;

    EAnestheticTechnique(Short id, String description) {
        this.id = id;
        this.description = description;
    }

    @JsonCreator
    public static List<EAnestheticTechnique> getAll() {
        return Stream.of(EAnestheticTechnique.values()).collect(Collectors.toList());
    }

    @JsonCreator
    public static EAnestheticTechnique map(Short id) {
        for (EAnestheticTechnique e : values()) {
            if (e.id.equals(id)) return e;
        }
        throw new NotFoundException("anesthetic-technique-type-not-exists", String.format("El tipo %s no existe", id));
    }
}
