package net.pladema.emergencycare.triage.service.domain.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import net.pladema.sgx.enums.MDSerializer;
import net.pladema.sgx.enums.MasterDataEnum;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@JsonSerialize(using = MDSerializer.class)
public enum EPerfusion implements MasterDataEnum {

    NORMAL(1, "Normal"),
    ALTERADA(2, "Alterada");

    private final Short id;
    private final String description;

    EPerfusion(Number id, String description) {
        this.id = id.shortValue();
        this.description = description;
    }

    @JsonCreator
    public static List<EPerfusion> getAll(){
        return Stream.of(EPerfusion.values()).collect(Collectors.toList());
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
