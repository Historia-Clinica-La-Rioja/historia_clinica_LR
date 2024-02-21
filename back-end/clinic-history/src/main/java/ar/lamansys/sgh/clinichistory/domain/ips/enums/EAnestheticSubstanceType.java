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
public enum EAnestheticSubstanceType {

    PRE_MEDICATION((short) 1, "Premedicación"),
    ANESTHETIC_PLAN((short) 2, "Plan anestésico"),
    ANALGESIC_TECHNIQUE((short) 3, "Técnica analgésica"),
    FLUID_ADMINISTRATION((short) 4, "Administración de líquido"),
    ANESTHETIC_AGENT((short) 5, "Agente anestésico"),
    NON_ANESTHETIC_DRUG((short) 6, "Droga no anestésica"),
    ANTIBIOTIC_PROPHYLAXIS((short) 7, "Profilaxis antibiótica"),
    ;

    private final Short id;
    private final String description;

    EAnestheticSubstanceType(Short id, String description) {
        this.id = id;
        this.description = description;
    }

    @JsonCreator
    public static List<EAnestheticSubstanceType> getAll() {
        return Stream.of(EAnestheticSubstanceType.values()).collect(Collectors.toList());
    }

    @JsonCreator
    public static EAnestheticSubstanceType map(Short id) {
        for (EAnestheticSubstanceType e : values()) {
            if (e.id.equals(id)) return e;
        }
        throw new NotFoundException("anesthetic-substance-type-not-exists", String.format("El tipo %s no existe", id));
    }
}
