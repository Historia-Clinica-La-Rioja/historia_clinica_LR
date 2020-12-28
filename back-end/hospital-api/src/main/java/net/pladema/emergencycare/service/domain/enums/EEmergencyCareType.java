package net.pladema.emergencycare.service.domain.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import net.pladema.sgx.enums.MDSerializer;
import net.pladema.sgx.enums.MasterDataEnum;

import java.util.Collection;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@JsonSerialize(using = MDSerializer.class)
public enum EEmergencyCareType implements MasterDataEnum {

    ADULTO(1, "Adulto"),
    PEDIATRIA(2, "Pediatría"),
    GINECOLOGICA(3, "Ginecológica y obstetricia");

    private final Short id;
    private final String description;

    EEmergencyCareType(Number id, String description) {
        this.id = id.shortValue();
        this.description = description;
    }

    @JsonCreator
    public static Collection<EEmergencyCareType> getAll(){
        return Stream.of(EEmergencyCareType.values()).collect(Collectors.toList());
    }

    @JsonValue
    public Short getId() {
        return id;
    }

    @JsonValue
    public String getDescription() {
        return description;
    }
}
